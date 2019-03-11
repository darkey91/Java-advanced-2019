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
 */
public class Implementor implements JarImpler {
    /** Suffix for generated file name */
    private final static String SUFFIX = "Impl";
    /** TAB for generated file name */
    private final static String TAB = "    ";
    private final static String DOUBLE_TAB = "        ";
    private final static String SPACE = " ";
    private final static String COMMA = ",";
    private final static String R_OPEN = "(";
    private final static String R_CLOSE = ")";
    private final static String CURLY_OPEN = "{";
    private final static String CURLY_CLOSE = "}";
    private final static String END_STRING = ";";
    private final static int MOD = (int) 1e9 + 1999;
    private final static String NEWLINE = System.lineSeparator();
    private final static String DOUBLE_NEWLINE = NEWLINE.concat(NEWLINE);
    private final static String COMMA_SPACE = COMMA.concat(SPACE);
    private final static String HEADER = CURLY_OPEN.concat(NEWLINE);
    private final static String FOOTER = CURLY_CLOSE.concat(NEWLINE);

    /**Default constructor*/
    public Implementor() {
    }

    private String getPackage(Class<?> token) {
        if (token.getPackage().getName().equals("")) {
            return "";
        }
        StringBuffer code = new StringBuffer("package".concat(SPACE));
        code.append(token.getPackage().getName()).append(END_STRING).append(DOUBLE_NEWLINE);
        return code.toString();
    }

    private String getClassName(Class<?> token) {
        return token.getSimpleName().concat(SUFFIX);
    }

    private Path getFilePathName(Class<?> token, Path path, String extension) {
        return path.resolve(token.getPackageName()
                .replace('.', File.separatorChar))
                .resolve(getClassName(token)
                        .concat("." + extension));
    }

    private void createDirectories(Path path) throws ImplerException {
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Can't create directories with path ".concat(path.toString()));
            }
        }
    }

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

    private String getDefaultValueString(Class<?> token) {
        if (token.equals(Boolean.TYPE)) {
            return " false";
        } else if (token.equals(Void.TYPE)) {
            return "";
        } else {
            return token.isPrimitive() ? " 0" : " null";
        }
    }

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
                    .append(END_STRING);
        } else {

            code.append("super".concat(R_OPEN));
            for (int i = 0; i < executable.getParameterCount(); i++) {
                if (i == executable.getParameterCount() - 1) {
                    code.append(String.format("var%d", i));
                } else {
                    code.append(String.format("var%d, ", i));
                }
            }
            code.append(R_CLOSE).append(END_STRING);
        }

        code.append(NEWLINE.concat(TAB).concat(FOOTER)).append(NEWLINE);
        return code.toString();
    }

    private class MethodSignatureComparator {
        private Method method;
        public MethodSignatureComparator(Method method) {
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other instanceof MethodSignatureComparator) {
                return other.hashCode() == hashCode();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return ((Arrays.hashCode(method.getParameterTypes()) * method.getParameterCount()) % MOD
                    + (method.getName().hashCode() * 37)) % MOD;
        }
    }

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


    private void fillSet(Method[] executables, Set<MethodSignatureComparator> methods) {
        methods.addAll(Arrays.stream(executables)
                .filter(m -> !Modifier.isFinal(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()) && Modifier.isAbstract(m.getModifiers()))
                .map(MethodSignatureComparator::new)
                .collect(Collectors.toList()));
    }

    private String getClassDeclarationTop(Class<?> token) {
        return "public class ".concat(getClassName(token))
                .concat(token.isInterface() ? " implements " : " extends ")
                .concat(token.getSimpleName()).concat(SPACE).concat(HEADER);
    }

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
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     *                                                                 generated.
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

    void compile(Class<?> token, Path tempPath) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String[] args = new String[]{ "-cp", tempPath.toString() + File.pathSeparator + System.getProperty("java.class.path"), getFilePathName(token, tempPath, "java").toString() };
        if (compiler == null || compiler.run(null, null, null, args) != 0) {
            throw new ImplerException(String.format("Unable to compile %s.java", getClassName(token)));
        }

    }

    void createJarFile(Class<?> token, Path tempDir, Path path) throws ImplerException {
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

    @Override
    public void implementJar(Class<?> token, Path path) throws ImplerException {
        validate(token);
        createDirectories(path);
        Path tempDir;

        try {
            tempDir = Files.createTempDirectory(path.toAbsolutePath().getParent(), "temp");
        } catch (IOException e) {
            throw new ImplerException("Unable to create temp directory", e);
        }

        try {
            implement(token, tempDir);
            compile(token, tempDir);
            createJarFile(token, tempDir, path);
        } catch (ImplerException e){
            System.err.println("Can't create jar file");
            return;
        }

    }

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args.length > 3 || args[0] == null || args[1] == null) {
            System.err.println("Wrong arguments");
            return;
        }

        JarImpler implementor = new Implementor();

        boolean jar = args.length > 2;

        try {
            if (jar) {
                implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            } else {
                if (args[2] == null) {
                    System.err.println("Wrong arguments. Usage : Implementor <Path> <Implementor> <Class>");
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

