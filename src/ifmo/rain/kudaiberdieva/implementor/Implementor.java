package ifmo.rain.kudaiberdieva.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;


/**
 *  Implementation class for <a href="hcodeps://www.kgeorgiy.info/git/geo/java-advanced-2019/src/master/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/JarImpler.java"> {@link JarImpler}</a> interface
 * for <a href="hcodeps://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Diana Kudaiberdieva
 * @version 1.0
 */
public class Implementor implements JarImpler {
    /** Suffix for the name of the file to create*/
    private final static String SUFFIX = "Impl";

    /** Symbol TAB for the file to create */
    private final static String TAB = "    ";

    /** Double TAB for the file to create*/
    private final static String DOUBLE_TAB = "        ";

    /** Space for the file to create */
    private final static String SPACE = " ";

    /** Comma for the file to create */
    private final static String COMMA = ",";

    /** Opening parenthesis for the file to create*/
    private final static String PARENTHESIS_OPEN = "(";

    /** Closing parenthesis for the file to create */
    private final static String PARENTHESIS_CLOSE = ")";

    /** Opening curly parenthesis for the file to create*/
    private final static String CURLY_OPEN = "{";

    /** Closing curly parenthesis for the file to create */
    private final static String CURLY_CLOSE = "}";

    /** Semicolon for the file to create */
    private final static String SEMICOLON = ";";

    /**Base for modulo operation*/
    private final static int MOD = (int) 1e9 + 1999;

    /**System line separator for the file to create*/
    private final static String NEWLINE = System.lineSeparator();

    /**Double system line separator for the file to create*/
    private final static String DOUBLE_NEWLINE = NEWLINE.concat(NEWLINE);

    /**Concatenation of comma and space for the file to create*/
    private final static String COMMA_SPACE = COMMA.concat(SPACE);

    /**Concatenation of opening curly parenthesis and line separator for the file to create*/
    private final static String HEADER = CURLY_OPEN.concat(NEWLINE);

    /**Concatenation of closing curly parenthesis and line separator for the file to create*/
    private final static String FOOTER = CURLY_CLOSE.concat(NEWLINE);

    /**Default constructor*/
    public Implementor() {}

    /**
     *Gets the package of this class. If the class is in an unnamed package returns empty string.
     * @param token represents class or interfaces which should be extended or implemented
     * @return The name of package
     */
    private String getPackage(Class<?> token) {
        if (token.getPackage().getName().equals("")) {
            return "";
        }
        StringBuffer code = new StringBuffer("package".concat(SPACE));
        code.append(token.getPackage().getName()).append(SEMICOLON).append(DOUBLE_NEWLINE);
        return code.toString();
    }

    /**
     * Returns the name for file to create. It contains name of the entity from parameters (class, interface) represented by {@link Class} object and {@link #SUFFIX}.
     *
     * @param  token represents class or interfaces which should be extended or implemented
     * @return The name of the file to create
     */
    private String getClassName(Class<?> token) {
        return token.getSimpleName().concat(SUFFIX);
    }

    /**
     * Returns path  to file to be created with specified extension.
     * The resulting string uses the default name-separator character to separate the names in the name sequence.
     * @param token represents class or interfaces in a running Java application
     * @param path path to parent directory for the file to be created
     * @param extension - string with necessary
     * @return The path represents path to the file to be created
     */
    private Path getFilePathName(Class<?> token, Path path, String extension) {
        return path.resolve(token.getPackageName()
                .replace('.', File.separatorChar))
                .resolve(getClassName(token)
                        .concat("." + extension));
    }

    /**
     * Create parent directory for the file to be created.
     *
     * @param path the parent directory to create
     * @throws ImplerException - If errors occur during creation
     */
    private void createDirectories(Path path) throws ImplerException {
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Can't create directories with path ".concat(path.toString()));
            }
        }
    }

    /**
     * Returns {@link String} with parameters with their canonical type names joining with comma and surrounded by parenthesises.
     * @param parameters are contained in result string
     * @return The string represents parameter list in method
     */
    private String getMethodParametersString(Class<?>[] parameters) {
        if (parameters.length == 0)
            return "() ";

        StringBuffer code = new StringBuffer("(");

        for (int i = 0; i < parameters.length; i++) {
            code.append(parameters[i].getCanonicalName());
            code.append(String.format(" var%d, ", i));
        }
        code.setCharAt(code.length() - 2, ')');
        return code.toString();
    }

    /**
     * Returns the string representation of default value for given {@link Class}.
     * @param token - {@link Class} to get default value
     * @return The string of default value.
     */
    private String getDefaultValueString(Class<?> token) {
        if (token.equals(Boolean.TYPE)) {
            return " false";
        } else if (token.equals(Void.TYPE)) {
            return "";
        } else {
            return token.isPrimitive() ? " 0" : " null";
        }
    }

    /**
     * Returns {@link String} that starts from "throws" and then enumerates exceptions could be thrown by executable from parameter.
     * @param executable to get possible exceptions
     * @return The string with exceptions for method code
     */
    private String getExceptionString(Executable executable) {
        StringBuffer code = new StringBuffer();

        Class<?>[] exceptions = executable.getExceptionTypes();

        if (exceptions.length == 0)
            return "";

        code.append("throws ")
                .append(Arrays.stream(exceptions)
                        .map(e -> e.getCanonicalName())
                        .collect(Collectors.joining(COMMA_SPACE)));

        return code.toString();

    }

    /**
     * Returns {@link String} that represent implementation code of method or constructor.
     * @param executable - to be implemented
     * @param isConstructor - to determine whether a constructor
     * @return The string with written code for method
     */
    private String createExecutableCode(Executable executable, boolean isConstructor) {
        int modifiers = executable.getModifiers() & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT & ~Modifier.NATIVE ;

        StringBuffer code = new StringBuffer(TAB.concat(Modifier.toString(modifiers)));

        Class<?> returnType = null;
        if (!isConstructor) {
            Method tmpMethod = (Method) executable;
            returnType = tmpMethod.getReturnType();
            code.append(SPACE.concat(returnType.getCanonicalName()))
                    .append(SPACE.concat(executable.getName()));
        } else {
            code.append(SPACE.concat(executable.getDeclaringClass().getSimpleName()));
            code.append(SUFFIX);
        }

        code.append(getMethodParametersString(executable.getParameterTypes()))
                .append(getExceptionString(executable))
                .append(HEADER)
                .append(DOUBLE_TAB);

        if (!isConstructor) {
            code.append("return ".concat(getDefaultValueString(returnType)))
                    .append(SEMICOLON);
        } else {

            code.append("super".concat(PARENTHESIS_OPEN));
            for (int i = 0; i < executable.getParameterCount(); i++) {
                if (i == executable.getParameterCount() - 1) {
                    code.append(String.format("var%d", i));
                } else {
                    code.append(String.format("var%d, ", i));
                }
            }
            code.append(PARENTHESIS_CLOSE).append(SEMICOLON);
        }

        code.append(NEWLINE.concat(TAB).concat(FOOTER)).append(NEWLINE);
        return code.toString();
    }

    /**
     * Nested class which helps to distinguish methods in connected classes or interfaces.
     * Methods with same signature can not appear in the code of the class being implemented, so {@link MethodSignatureComparator} is able to compare for equality them.
     */

    private class MethodSignatureComparator {
        /**
         * Method represented by {@link MethodSignatureComparator}
         */
        private Method method;

        /**
         * Creates instance of {@link MethodSignatureComparator}
         * @param method to be wrapped by {@link MethodSignatureComparator}
         */
        public MethodSignatureComparator(Method method) {
            this.method = method;
        }

        /**
         * Returns the represented method
         * @return method
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Compares for equality two instances of {@link MethodSignatureComparator}
         * @param other to compare with this
         * @return True - if equal. False - If not equals or other is null, or other is not instance of {@link MethodSignatureComparator}
         */
        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other instanceof MethodSignatureComparator) {
                return other.hashCode() == hashCode();
            }
            return false;
        }

        /**
         * Calculate hash code for method. Hash code depends on method signature.
         * @return hash code of methdd
         */

        @Override
        public int hashCode() {
            return ((Arrays.hashCode(method.getParameterTypes()) * method.getParameterCount()) % MOD
                    + (method.getName().hashCode() * 37)) % MOD;
        }
    }

    /**
     * Returns string that contain code of implementation of constructors for class to be created.
     * @param token {@link Class} to be extended or implemented
     * @return The code for constructors
     * @throws ImplerException - If token is not interface and does't contain any constructor
     */
    private String generateConstructors(Class<?> token) throws ImplerException {
        StringBuffer code = new StringBuffer();
        Constructor[] constructors = Arrays.stream(token.getDeclaredConstructors())
                .filter(c -> !Modifier.isPrivate(c.getModifiers()))
                .toArray(Constructor[]::new);

        if (constructors.length == 0)
            throw new ImplerException(String.format("Class %s should have callable constructors", constructors.getClass().getName()));

        for (Constructor c: constructors) {
            code.append(createExecutableCode(c, true));
        }
        return code.toString();
    }

    /**
     * Returns string that contain code of implementation of methods and constructors for class to be created.
     * @param token {@link Class} to be extended or implemented
     * @return The code for constructors and methods
     * @throws ImplerException - If token is not interface and does't contain any constructor
     */

    private String implementExecutables(Class<?> token) throws ImplerException  {
        StringBuffer code = new StringBuffer();

        if (!token.isInterface()) {
            code.append(generateConstructors(token));
        }

        Set<MethodSignatureComparator> methods = new HashSet<>();
        fillSet(token.getMethods(), methods);

        while (token != null) {
            fillSet(token.getDeclaredMethods(), methods);
            token = token.getSuperclass();
        }

        for (MethodSignatureComparator m: methods) {
            code.append(createExecutableCode(m.getMethod(), false));
        }
        return code.toString();
    }

    /**
     * Fills set with uniques methods. Set is maintained by {@link MethodSignatureComparator}.
     * @param executables array of methods for filling
     * @param methods set to be filled
     */

    private void fillSet(Method[] executables, Set<MethodSignatureComparator> methods) {
        methods.addAll(Arrays.stream(executables)
                .filter(m -> !Modifier.isFinal(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()) && Modifier.isAbstract(m.getModifiers()))
                .map(MethodSignatureComparator::new)
                .collect(Collectors.toList()));
    }

    /**
     * Returns {@link String} that represents signature of class declaration code.
     * @param token to create name for class based on name of token
     * @return The string with class declaration
     */

    private String getClassDeclarationTop(Class<?> token) {
        return "public class ".concat(getClassName(token))
                .concat(token.isInterface() ? " implements " : " extends ")
                .concat(token.getSimpleName()).concat(SPACE).concat(HEADER);
    }

    /**
     * Checks if given {@link Class} can be implemented or extended
     * @param token to be implemented or extended
     * @throws ImplerException - if {@link Class} can not be implemented or extended
     */
    private void validate(Class<?> token) throws ImplerException {
        if (token.isPrimitive() || token.isArray() || token.equals(void.class) || Modifier.isFinal(token.getModifiers())
                || token == Enum.class ){
            throw new ImplerException("Invalid class ".concat(token.getCanonicalName().concat(" was given.%n")));
        }
    }

    /**
     * Produces code implementing class or interface specified by provided <code>token</code>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <code>Impl</code> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <code>root</code> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <code>$root/java/util/ListImpl.java</code>
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException when implementation cannot be generated.
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        validate(token);

        try {
            root = getFilePathName(token, root, "java");
            createDirectories(root);
        } catch (InvalidPathException e) {
            System.err.println(String.format("Can't create pat %s", root.toString()));
            return;
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(root)) {
            StringBuilder classCode = new StringBuilder(getPackage(token));
            classCode.append(getClassDeclarationTop(token));
            classCode.append(implementExecutables(token));
            classCode.append(FOOTER);
            bufferedWriter.write(classCode.toString());
        } catch (IOException e) {
            throw new ImplerException("...");
        }
    }

    /**
     * Compile created class to temporary directory. Temporary directory is situated in the same folder with specified path.
     * @param token to get name for class to be compiled
     * @param tempPath where pu compiled classes
     * @throws ImplerException - If unable to compile class
     */
    private void compile(Class<?> token, Path tempPath) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String[] args = new String[]{ "-cp", tempPath.toString() + File.pathSeparator + System.getProperty("java.class.path"),
                getFilePathName(token, tempPath, "java").toString() };
        if (compiler == null || compiler.run(null, null, null, args) != 0) {
            throw new ImplerException(String.format("Unable to compile %s.java", getClassName(token)));
        }

    }

    /**
     * Creates jar file with compiled class.
     * @param token class to compile
     * @param tempDir where to compile
     * @param path temporaryr directory is situated
     * @throws ImplerException can't create jar file
     */

    private void createJarFile(Class<?> token, Path tempDir, Path path) throws ImplerException {
        Manifest manifest = new Manifest();
        Attributes manifestAttributes = manifest.getMainAttributes();
        manifestAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifestAttributes.put(Attributes.Name.IMPLEMENTATION_VENDOR, "Diana Kudaiberdieva");

        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(path), manifest)) {
            jos.putNextEntry(new ZipEntry(token.getName().replace('.', '/') + SUFFIX + ".class".trim()));
            Files.copy(getFilePathName(token, tempDir, "class"), jos);
        } catch (IOException e) {
            throw new ImplerException("Can't create JAR file", e);
        }
    }
    /**
     * Produces <code>.jar</code> file implementing class or interface specified by provided <code>token</code>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <code>Impl</code> suffix
     * added.
     *
     * @param token type token to create implementation for.
     * @param jarFile target <code>.jar</code> file.
     * @throws ImplerException when implementation cannot be generated.
     */

    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        validate(token);
        createDirectories(jarFile);
        Path tempDir;

        try {
            tempDir = Files.createTempDirectory(jarFile.toAbsolutePath().getParent(), "temp");
        } catch (IOException e) {
            throw new ImplerException("Unable to create temp directory", e);
        }

        try {
            implement(token, tempDir);
            compile(token, tempDir);
            createJarFile(token, tempDir, jarFile);
        } catch (ImplerException e){
            System.err.println("Can't create jar file");
        }

    }

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args.length > 3 || args[0] == null || args[1] == null) {
            System.err.println("Wrong arguments");
            return;
        }

        JarImpler implementor = new Implementor();

        try {
            if (args.length == 2) {
                implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            } else {
                if (args[2] == null) {
                    System.err.println("Wrong arguments. Usage : Implementor <Implementor> <Class> <Path>");
                    return;
                }
                implementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            }
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("Class %s was not found%n", args[0]));
        } catch (ImplerException e) {
            System.err.println(String.format("%S...%n", args[0]));
        }
    }
}

