package com.ponya.handlerbarsexample.templates;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.ponya.handlerbarsexample.helper.HelperSource;
import com.ponya.handlerbarsexample.model.Person;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BasicTemplate {

    TemplateLoader baseHandlebarsLoader = new ClassPathTemplateLoader("/handlebars", ".hbs");


    @Test
    public void whenThereIsNoTemplateFile_ThenComilesInline() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hi {{ this }}!");
        String templateString = template.apply("Baeldung");

        assertThat(templateString, is("Hi Baeldung!"));
    }

    @Test
    public void whenParameterMapIsSupplied_thenDisplays() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hi {{name}}!");
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("name", "Baeldung");
        String templateString = template.apply(parameterMap);

        assertThat(templateString, is("Hi Baeldung!"));
    }

    @Test
    public void whenParameterObjectIsSupplied_ThenDisplays() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hi {{name}}!");
        Person person = new Person("Baeldung");
        String templateString = template.apply(person);

        assertThat(templateString, is("Hi Baeldung!"));
    }

    @Test
    public void whenNoLoaderIsGiven_ThenSearchesClasspath() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile("handlebars/greeting");
        Person person = new Person("Baeldung");
        String templateString = template.apply(person);

        assertThat(templateString, is("Hi Baeldung!"));
    }

    @Test
    public void whenClasspathTemplateLoaderIsGiven_ThenSearchesClasspathWithPrefixSuffix() throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader("/handlebars", ".html");
        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile("greeting");
        Person person = new Person("Baeldung");
        String templateString = template.apply(person);

        assertThat(templateString, is("Hi Baeldung!"));
    }

    @Test
    public void whenMultipleLoaderAreGiven_ThenSearchesSequentially() throws IOException {
        TemplateLoader firstLoader = new ClassPathTemplateLoader("/handlebars", ".html");
        TemplateLoader secondLoader = new ClassPathTemplateLoader("/templates", ".html");
        Handlebars handlebars = new Handlebars().with(firstLoader, secondLoader);
        Template template = handlebars.compile("greeting2");
        Person person = new Person("Baeldung");
        String templateString = template.apply(person);

        assertThat(templateString, is("Hi Baeldung2!"));
    }

    @Test
    public void whenUsedWith_ThenContextChanges() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        Template template = handlebars.compile("with");
        Person person = new Person("Baeldung");
        person.getAddress().setStreet("World");
        String templateString = template.apply(person);

        assertThat(templateString, is("<h4>I live in World</h4>"));
    }

    @Test
    public void whenUsedEach_ThenIterates() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        Template template = handlebars.compile("each");
        Person person = new Person("Baeldung");
        Person friend1 = new Person("Java");
        Person friend2 = new Person("Spring");
        person.getFriends().add(friend1);
        person.getFriends().add(friend2);
        String templateString = template.apply(person);

        assertThat(templateString, containsString("Java is my friend."));
        assertThat(templateString, containsString("Spring is my friend."));
    }

    @Test
    public void whenUsedIf_ThenPutsCondition() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        Template template = handlebars.compile("if");
        Person person = new Person("Baeldung");
        person.setBusy(true);
        String templateString = template.apply(person);

        assertThat(templateString, containsString("Baeldung is busy."));
    }

    @Test
    public void whenHelperIsCreated_ThenCanRegister() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        handlebars.registerHelper("isBusy", new Helper<Person>() {
            @Override
            public Object apply(Person context, Options options) throws IOException {
                String busyString = context.isBusy() ? "busy" : "available";
                return context.getName() + " - " + busyString;
            }
        });
        Template template = handlebars.compile("isBusy");
        Person person = new Person("Ponya");
        person.setBusy(true);
        String templateString = template.apply(person);

        assertThat(templateString, containsString("Ponya - busy"));
    }

    @Test
    public void whenHelperSourceIsCreated_ThenCanRegister() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        handlebars.registerHelpers(new HelperSource());
        Template template = handlebars.compile("isBusy");
        Person person = new Person("Ponya");
        person.setBusy(true);
        String templateString = template.apply(person);

        assertThat(templateString, containsString("Ponya - busy"));
    }

    @Test
    public void whenOtherTemplateIsReferenced_ThenCanReuse() throws IOException {
        Handlebars handlebars = new Handlebars(baseHandlebarsLoader);
        Template template = handlebars.compile("page");
        Person person = new Person("Ponya");
        String templateString = template.apply(person);

        assertThat(templateString, containsString("Hi Ponya!"));
        assertThat(templateString, containsString("This is the page Ponya"));
    }
}
