package ru.indutranslator.ai.prompt;

import java.util.HashMap;
import java.util.Map;

/**
 * Prompt template for AI generation.
 * Supports placeholders for dynamic content.
 */
public class PromptTemplate {
    
    private final String template;
    private final Map<String, String> defaultValues = new HashMap<>();
    
    private PromptTemplate(String template) {
        this.template = template;
    }
    
    /**
     * Create new prompt template.
     * @param template template string with placeholders like {placeholder}
     * @return PromptTemplate instance
     */
    public static PromptTemplate of(String template) {
        return new PromptTemplate(template);
    }
    
    /**
     * Set default value for placeholder.
     * @param placeholder placeholder name
     * @param value default value
     * @return this
     */
    public PromptTemplate withDefault(String placeholder, String value) {
        defaultValues.put(placeholder, value);
        return this;
    }
    
    /**
     * Render template with values.
     * @param values values for placeholders
     * @return rendered prompt
     */
    public String render(Map<String, String> values) {
        String result = template;
        
        // Replace with provided values first
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        // Replace with defaults for remaining placeholders
        for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, entry.getValue());
            }
        }
        
        return result;
    }
    
    /**
     * Render template with single value.
     * @param placeholder placeholder name
     * @param value value
     * @return rendered prompt
     */
    public String render(String placeholder, String value) {
        return render(placeholder, value, new HashMap<>());
    }
    
    /**
     * Render template with single value and additional values.
     * @param placeholder primary placeholder
     * @param value primary value
     * @param additionalValues additional placeholder values
     * @return rendered prompt
     */
    public String render(String placeholder, String value, Map<String, String> additionalValues) {
        Map<String, String> values = new HashMap<>(additionalValues);
        values.put(placeholder, value);
        return render(values);
    }
    
    /**
     * Get template string.
     * @return template
     */
    public String getTemplate() {
        return template;
    }
}
