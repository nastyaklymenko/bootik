package com.example.utils.creators;

import com.example.config.JazzyConfig;
import com.example.utils.spellcheckers.JazzySpellChecker;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;
import com.microsoft.bot.schema.models.Attachment;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = {JazzySpellChecker.class, JazzyConfig.class})
public class ActivityCreator {

  private static final String spellCheckedResponsePart = "You have probably meant: ";
  private static final String echoResponsePart = "You typed: ";

  private static ApplicationContext context = new AnnotationConfigApplicationContext(
      JazzySpellChecker.class);

  private static JazzySpellChecker spellChecker = (JazzySpellChecker) context
      .getBean(JazzySpellChecker.class);

  private ActivityCreator() {

  }

  public static Activity createSpellCheckedActivity(Activity activity) {
    return createEmptyActivity(activity)
        .withText(spellCheckedResponsePart + spellChecker.getCorrectedText(activity.text()));
  }

  public static Activity createEchoActivity(Activity activity) {
    return createEmptyActivity(activity)
        .withText(echoResponsePart + activity.text());
  }

  public static Activity createActivityWithAttachments(Activity activity) {
    List<Attachment> attachments = activity.attachments();
    return createEmptyActivity(activity)
        .withText("types of attachments: " +
            attachments.stream()
                .map(Attachment::contentType)
                .collect(Collectors.toList())
                .toString())
        .withAttachments(attachments);
  }

  private static Activity createEmptyActivity(Activity activity) {
    return new Activity()
        .withType(ActivityTypes.MESSAGE)
        .withRecipient(activity.from())
        .withFrom(activity.recipient());
  }

}
