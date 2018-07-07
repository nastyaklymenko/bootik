package com.example.controllers;

import static com.example.utils.ActivityUtils.containsAttachments;
import static com.example.utils.ActivityUtils.getAttachmentsByType;

import com.example.utils.ActivityUtils;
import com.example.utils.creators.ActivityCreator;
import com.example.utils.creators.ConversationCreator;
import com.example.utils.senders.ResourceResponseSender;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.Conversations;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.Attachment;
import com.microsoft.bot.schema.models.ResourceResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messages/bot")
public class BotMessagesController {

  @Autowired
  private MicrosoftAppCredentials credentials;

  @Autowired
  private List<ResourceResponse> responses;

  @PostMapping(path = "")
  public List<ResourceResponse> create(@RequestBody @Valid
  @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClient connector =
        new ConnectorClientImpl(activity.serviceUrl(), credentials);

    Conversations conversation = ConversationCreator.createResponseConversation(connector);

    if (containsAttachments(activity)) {
      List<Attachment> attachments = getAttachmentsByType(activity, "image");
      Activity activityWithAttachments =
          ActivityCreator.createActivityWithAttachments(activity.withAttachments(attachments));

      ResourceResponse responseWithAttachments
          = ResourceResponseSender.send(conversation, activity, activityWithAttachments);

      responses.add(responseWithAttachments);

    } else {
      Activity echoActivity = ActivityCreator.createEchoActivity(activity);
      Activity checkedActivity = ActivityCreator.createSpellCheckedActivity(activity);

      ResourceResponse echoResponse =
          ResourceResponseSender.send(conversation, activity, echoActivity);
      responses.add(echoResponse);

      ResourceResponse spellCheckedResponse =
          ResourceResponseSender.send(conversation, activity, checkedActivity);
      responses.add(spellCheckedResponse);
    }
    return responses;
  }
}

