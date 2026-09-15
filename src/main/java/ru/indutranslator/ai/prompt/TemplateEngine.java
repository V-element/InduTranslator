package ru.indutranslator.ai.prompt;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Template engine for managing and rendering prompt templates.
 */
@Component
public class TemplateEngine {
    
    private final Map<String, PromptTemplate> templates = new HashMap<>();
    
    public TemplateEngine() {
        initDefaultTemplates();
    }
    
    /**
     * Initialize default prompt templates.
     */
    private void initDefaultTemplates() {
        // Document analysis template
        templates.put("document_analysis", PromptTemplate.of(
            "You are an expert document analyst. Analyze the following document and provide a summary.\n" +
            "Document content:\n" +
            "{document_content}\n" +
            "Please provide:\n" +
            "1. Key points\n" +
            "2. Main theme\n" +
            "3. Important entities mentioned"
        ).withDefault("max_length", "500 words"));
        
        // Text translation template
        templates.put("translation", PromptTemplate.of(
            "Translate the following text to {target_language}.\n" +
            "Original text:\n" +
            "{text}\n" +
            "Maintain the tone and style of the original."
        ));
        
        // Question answering template
        templates.put("qa", PromptTemplate.of(
            "Answer the following question based on the context.\n" +
            "Context:\n" +
            "{context}\n" +
            "Question:\n" +
            "{question}\n" +
            "Answer:"
        ));
        
        // Summarization template
        templates.put("summarization", PromptTemplate.of(
            "Summarize the following text in {num_points} bullet points.\n" +
            "Text:\n" +
            "{text}\n" +
            "Summary:"
        ).withDefault("num_points", "5"));
        
        // Code explanation template
        templates.put("code_explanation", PromptTemplate.of(
            "Explain the following code.\n" +
            "Code:\n" +
            "{code}\n" +
            "Explanation:"
        ));
    }
    
    /**
     * Register custom template.
     * @param name template name
     * @param template prompt template
     */
    public void registerTemplate(String name, PromptTemplate template) {
        templates.put(name, template);
    }
    
    /**
     * Get template by name.
     * @param name template name
     * @return prompt template
     */
    public PromptTemplate getTemplate(String name) {
        PromptTemplate template = templates.get(name);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + name);
        }
        return template;
    }
    
    /**
     * Render template with values.
     * @param name template name
     * @param values values for placeholders
     * @return rendered prompt
     */
    public String renderTemplate(String name, Map<String, String> values) {
        return getTemplate(name).render(values);
    }
    
    /**
     * Render template with single value.
     * @param name template name
     * @param placeholder placeholder
     * @param value value
     * @return rendered prompt
     */
    public String renderTemplate(String name, String placeholder, String value) {
        return getTemplate(name).render(placeholder, value);
    }
    
    /**
     * Get all template names.
     * @return list of template names
     */
    public java.util.List<String> getAllTemplateNames() {
        return new java.util.ArrayList<>(templates.keySet());
    }
}
