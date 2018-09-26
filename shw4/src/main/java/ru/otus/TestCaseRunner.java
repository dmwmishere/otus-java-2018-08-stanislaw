package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCaseRunner {
    private int failed = 0, passed = 0, skipped = 0;
    private final Class<?> testCase;

    public enum TestMode {SINGLE_STEP, MULTI_STEP};

    public TestCaseRunner(final Class<?> testCase){
        this.testCase = testCase;
    }

    public void runTest(final TestMode mode){
        List<Method> before = methodsByAnnotation(testCase, Before.class);

        List<Method> testStep = methodsByAnnotation(testCase, Test.class);
        testStep.sort((m1, m2) -> { // execute test steps in defined order
            int order1 = m1.getAnnotation(Test.class).order(),
                    order2 = m2.getAnnotation(Test.class).order();

            if (order1 == order2) return 0;
            return order1 < order2 ? -1 : 1;
        });

        List<Method> after = methodsByAnnotation(testCase, After.class);

        try {

            switch (mode){
                case SINGLE_STEP:

                    for(Method step : testStep){
                        System.out.println("----------- " + step.getName() + " -----------");

                        if(failed > 0){
                            skipped++;
                        } else {
                            Object obj = testCase.newInstance();

                            for (Method method : before) {
                                // skip test case execution if there are any errors before test steps
                                if (!executeTest(method, obj)) return;
                            }

                            if (executeTest(step, obj)) passed++;
                            else failed++;

                            for (Method method : after) {
                                executeTest(method, obj);
                            }
                        }

                    }

                    break;

                case MULTI_STEP:

                    Object obj = testCase.newInstance();

                    for (Method method : before) {
                        // skip test case execution if there are any errors before test steps
                        if (!executeTest(method, obj)) return;
                    }

                    for (Method method : testStep) {
                        if (failed > 0) { // skip all steps after any failures occurred
                            skipped++;
                        } else {
                            if (executeTest(method, obj)) passed++;
                            else failed++;
                        }
                    }

                    for (Method method : after) {
                        executeTest(method, obj);
                    }
            }

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }


    private boolean executeTest(Method method, Object instance) throws IllegalAccessException {
        try {
            method.invoke(instance);
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.err.printf("test step %s failed with error: %s\n", method.getName(), e.getCause());
            return false;
        }
    }

    private List<Method> methodsByAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
        return Stream.of(cls.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public int getFailed() {
        return failed;
    }

    public int getPassed() {
        return passed;
    }

    public int getSkipped() {
        return skipped;
    }
}
