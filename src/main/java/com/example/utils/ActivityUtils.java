package com.example.utils;

import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.Attachment;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityUtils {

  public static List<Attachment> getAttachmentsByType(Activity activity, String type) {
    return activity.attachments()
        .stream()
        .filter(i ->
            i.contentType().contains(type))
        .collect(Collectors.toList());
  }

  public static boolean containsAttachments(Activity activity) {
    return activity.attachments() != null;
  }

}
