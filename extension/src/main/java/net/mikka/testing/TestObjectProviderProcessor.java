package net.mikka.testing;

import com.google.auto.service.AutoService;
import lombok.SneakyThrows;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.Set;

@SuppressWarnings("unused")
@SupportedAnnotationTypes("net.mikka.testing.annotations.TestObjectProvider")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TestObjectProviderProcessor extends AbstractProcessor {
    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            final Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                generateTestClass(annotatedElement);
            }
        }
        return true;
    }

    @SneakyThrows
    private void generateTestClass(Element element) {

        final String packageName = element.getEnclosingElement().toString();
        final String className = element.getSimpleName() + "Test";
        final String testObjectProviderClassName = packageName + "." + element.getSimpleName();


        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            out.println("package " + packageName + ";");
            out.println();

            out.println("import javax.annotation.processing.Generated;");
            out.println("import org.junit.jupiter.api.Assertions;");
            out.println("import org.junit.jupiter.api.Test;");
            out.println("import " + TestObjectScanner.class.getName() + ";");
            out.println("import " + ValidationError.class.getName() + ";");
            out.println("import java.util.stream.Collectors;;");
            out.println("import java.util.List;");
            out.println();
            out.println("@Generated(\"net.mikka.testing.TestObjectProviderProcessor\")");
            out.println("class " + className + " {");
            out.println();
            out.println("  @Test");
            out.println("  void shouldHaveValidTestObjects() {");
            out.println("    final List<ValidationError> validationErrors = TestObjectScanner.validateTestObjects(" + element.getSimpleName() + ".class);");
            out.println("    Assertions.assertEquals(0, validationErrors.size(), () -> \"The following problems were found in " + testObjectProviderClassName + ": \\n\" + validationErrors.stream().map(Object::toString).collect(Collectors.joining(\"\\n\")));");
            out.println("  }");
            out.println();
            out.println("}");
        }
    }
}
