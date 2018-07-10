package com.example.config;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class JazzyConfig {

  @Value("${jazzy.dictionarypath}")
  private String path;

  @Bean
  public File getDictionary() {
    return new File(path);
  }

  @Bean(name = "dict")
  public SpellDictionaryHashMap getDictionaryMap() {
    try {
      return new SpellDictionaryHashMap(getDictionary());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Bean
  public SpellChecker getSpellChecker() {
    return new SpellChecker(getDictionaryMap());
  }

  @Bean
  public List<String> getMisspelledWords() {
    return new ArrayList<>();
  }

}
