package ifmo.rain.kudaiberdieva.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Implementor extends ClassLoader implements Impler {
    private final static String SUFFIX = "Impl";
    private final static String PACKAGE = "package";
    private final static String JAVA_EXTENSION = ".java";
    private final static String SUPER = "super";
    private final static String TAB = "    ";
    private final static String DOUBLE_TAB = "        ";
    private final static String SPACE = " ";
    private final static String COMMA = ",";
    private final static String R_OPEN = "(";
    private final static String R_CLOSE = ")";
    private final static String C_OPEN = "{";
    private final static String C_CLOSE = "}";
    private final static String END_STRING = ";";
    private final static int MOD = (int) 1e9 + 1999;
    private final static String NEWLINE = System.lineSeparator();
    private final static String DOUBLE_NEWLINE = NEWLINE.concat(NEWLINE);
    private final static String COMMA_SPACE = COMMA.concat(SPACE);
    private final static String HEADER = C_OPEN.concat(NEWLINE);
    private final static String FOOTER = C_CLOSE.concat(NEWLINE);

    public Implementor() {
    }

    private String getPackage(Class<?> token) {
        if (token.getPackage().getName().equals("")) {
            return "";
        }
        StringBuffer code = new StringBuffer(PACKAGE.concat(SPACE));
        code.append(token.getPackage().getName()).append(END_STRING).append(DOUBLE_NEWLINE);
        return code.toString();
    }

    private String getClassName(Class<?> token) {
        return token.getSimpleName().concat(SUFFIX);
    }

    private Path getFilePathName(Class<?> token, Path path) {
        return path.resolve(token.getPackageName()
                .replace('.', File.separatorChar))
                .resolve(getClassName(token)
                        .concat(JAVA_EXTENSION));
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

    private String createExecutableCode(Executable executable, boolean isConstructor) {
        int modifiers = executable.getModifiers() & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT & ~Modifier.NATIVE;

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
                .append(HEADER)
                .append(DOUBLE_TAB);

        if (!isConstructor) {
            code.append("return ".concat(getDefaultValueString(returnType)))
                    .append(END_STRING);
        } else {

            code.append(SUPER.concat(R_OPEN));
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

    private void fillMethodSet(Class<?> clazz, Set<MethodSignatureComparator> methods) {
        if (clazz == null) return;
        fillMethodSet(clazz.getSuperclass(), methods);
        Arrays.stream(clazz.getInterfaces()).forEach((i) -> fillMethodSet(i, methods));
        methods.addAll(
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(m -> Modifier.isAbstract(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()))
                        .map(MethodSignatureComparator::new).
                        collect(Collectors.toSet())
        );
    }

    private String implementExecutables(Class<?> token) {
        StringBuffer code = new StringBuffer();
        if (!token.isInterface()) {
            code.append(implementExecutables(token.getDeclaredConstructors(), true));
        }

        Set<MethodSignatureComparator> methods = new HashSet<>();
        fillMethodSet(token, methods);
        methods.forEach(m -> code.append(createExecutableCode(m.getMethod(), false)));
        return code.toString();
    }

    private String implementExecutables(Executable[] executables, boolean areConstructors) {
        StringBuffer code = new StringBuffer();
        Arrays.stream(executables)
                .filter(e -> !Modifier.isPrivate(e.getModifiers()))
                .forEach((e) -> {
                    code.append(createExecutableCode(e, areConstructors).concat(NEWLINE));
                });
        return code.toString();
    }

    private String getClassDeclarationTop(Class<?> token) {
        return "public class ".concat(getClassName(token))
                .concat(token.isInterface() ? " implements " : " extends ")
                .concat(token.getSimpleName()).concat(SPACE).concat(HEADER);
    }

    private void validate(Class<?> token) throws ImplerException {
        if (token.isPrimitive() || token.equals(void.class) || Modifier.isFinal(token.getModifiers()) || token.equals(Enum.class)) {
            throw new ImplerException("Invalid class ".concat(token.getCanonicalName().concat(" was given.%n")));
        }
    }

    /**
     * Produces code implementing class or interface specified by provided <tt>token</tt>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <tt>Impl</tt> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <tt>root</tt> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <tt>$root/java/util/ListImpl.java</tt>
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     *                                                                 generated.
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        validate(token);

        root = getFilePathName(token, root);
        createDirectories(root);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(root)) {
            StringBuilder classCode = new StringBuilder(getPackage(token));
            classCode.append(getClassDeclarationTop(token));
            classCode.append(implementExecutables(token));
            classCode.append(FOOTER);
            bufferedWriter.write(classCode.toString());
            System.out.println(classCode.toString());
        } catch (IOException e) {
            throw new ImplerException("...");
        }

    }

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Wrong arguments. Usage : Implementor <Class name> <Path>");
            return;
        }

        Impler implementor = new Implementor();
        try {
            implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("Class %s was not found%n", args[0]));
        } catch (ImplerException e) {
            System.err.println(String.format("...%n", args[0]));
        }
    }
}
