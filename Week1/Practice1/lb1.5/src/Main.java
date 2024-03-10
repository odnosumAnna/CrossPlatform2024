import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Evaluatable {
    double eval(double x);
}

class F1 implements Evaluatable {
    @Override
    public double eval(double x) {
        return Math.exp(-Math.abs(2.5 * x)) * Math.sin(x);
    }
}

class F2 implements Evaluatable {
    @Override
    public double eval(double x) {
        return x * x;
    }
}

class ProfilingHandler implements InvocationHandler {
    private final Object target;

    public ProfilingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        Object result = method.invoke(target, args);
        long end = System.nanoTime();
        System.out.println(method.getName() + " took " + (end - start) + " ns");
        return result;
    }
}

class TracingHandler implements InvocationHandler {
    private final Object target;

    public TracingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print(method.getName() + "(");
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(args[i]);
            }
        }
        System.out.println(")");
        Object result = method.invoke(target, args);
        System.out.println(method.getName() + " = " + result);
        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        Evaluatable f1 = new F1();
        Evaluatable f2 = new F2();

        Evaluatable f1ProxyProfiling = (Evaluatable) Proxy.newProxyInstance(
                Main.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new ProfilingHandler(f1)
        );

        Evaluatable f1ProxyTracing = (Evaluatable) Proxy.newProxyInstance(
                Main.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new TracingHandler(f1)
        );

        Evaluatable f2ProxyProfiling = (Evaluatable) Proxy.newProxyInstance(
                Main.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new ProfilingHandler(f2)
        );

        Evaluatable f2ProxyTracing = (Evaluatable) Proxy.newProxyInstance(
                Main.class.getClassLoader(),
                new Class[]{Evaluatable.class},
                new TracingHandler(f2)
        );

        System.out.println("F1: " + f1ProxyProfiling.eval(0.5));
        System.out.println("F2: " + f2ProxyProfiling.eval(1.0));

        System.out.println("F1: " + f1ProxyTracing.eval(0.5));
        System.out.println("F2: " + f2ProxyTracing.eval(1.0));
    }
}
