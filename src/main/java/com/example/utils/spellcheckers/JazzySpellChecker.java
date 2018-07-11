package com.example.utils.spellcheckers;

import com.example.config.JazzyConfig;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JazzySpellChecker implements SpellCheckListener {

  private ApplicationContext context = new AnnotationConfigApplicationContext(JazzyConfig.class);

  private SpellChecker spellChecker = context.getBean(SpellChecker.class);

  private List<String> misspelledWords = context.getBean(List.class);

  public JazzySpellChecker() {
    spellChecker.addSpellCheckListener(this);
  }

  public List<String> getMisspelledWords(String text) {
    StringWordTokenizer texTok = new StringWordTokenizer(text,
        new TeXWordFinder());
    spellChecker.checkSpelling(texTok);
    return misspelledWords;
  }

  public String getCorrectedLine(String line) {
    List<String> misSpelledWords = getMisspelledWords(line);

    for (String misSpelledWord : misSpelledWords) {
      List<String> suggestions = getSuggestions(misSpelledWord);
      if (suggestions.size() == 0) {
        continue;
      }
      String bestSuggestion = suggestions.get(0);
      line = line.replace(misSpelledWord, bestSuggestion);
    }

    return line;
  }

  public String getCorrectedText(String line) {
    StringBuilder builder = new StringBuilder();
    String[] tempWords = line.split(" ");

    for (String tempWord : tempWords) {
      if (!spellChecker.isCorrect(tempWord)) {
        List<Word> suggestions = spellChecker.getSuggestions(tempWord, 0);
        if (suggestions.size() > 0) {
          builder.append(spellChecker.getSuggestions(tempWord, 0).get(0).toString());
        } else {
          builder.append(tempWord);
        }
      } else {
        builder.append(tempWord);
      }
      builder.append(" ");
    }

    return builder.toString().trim();
  }

  public List<String> getSuggestions(String misspelledWord) {

    @SuppressWarnings("unchecked")
    List<Word> suggestedWords = spellChecker.getSuggestions(misspelledWord, 0);
    List<String> suggestions = new ArrayList<>();

    suggestedWords.forEach(i -> suggestions.add(i.getWord()));

    return suggestions;
  }

  public void spellingError(SpellCheckEvent event) {
    event.ignoreWord(true);
    misspelledWords.add(event.getInvalidWord());
  }

}