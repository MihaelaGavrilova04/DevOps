package com.frontend.user.parser;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

class ParserCSVTest {

    @Test
    void testClassExists() {
        Class<?> parserClass = ParserCSV.class;
        assertNotNull(parserClass);
        assertEquals("ParserCSV", parserClass.getSimpleName());
    }

    @Test
    void testImplementsParserInterface() {
        Class<?>[] interfaces = ParserCSV.class.getInterfaces();
        boolean found = false;
        for (Class<?> iface : interfaces) {
            if (iface.getSimpleName().equals("Parser")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testExpectedLengthConstant() throws Exception {
        Field field = ParserCSV.class.getDeclaredField("EXPECTED_LENGTH");
        int modifiers = field.getModifiers();
        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        field.setAccessible(true);
        int value = field.getInt(null);
        assertEquals(3, value);
    }

    @Test
    void testHasPostConstructAnnotation() throws Exception {
        Method method = ParserCSV.class.getDeclaredMethod("populate");
        var annotations = method.getAnnotations();
        boolean found = false;
        for (var annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("PostConstruct")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testClassInCorrectPackage() {
        String packageName = ParserCSV.class.getPackageName();
        assertEquals("com.frontend.user.parser", packageName);
    }
}