<div class="parent">
  [#assign pollSelector = state.getSelector()!]
  [#if pollSelector?has_content]

    [#assign targetPoll = cmsfn.contentByPath("/"+pollSelector, "polls")!]
    [#if targetPoll?has_content]

      [#--  
        For handling the not completely deleted poll issue, 
        will be removed after completing FA-25.
      --]
      [#if targetPoll.@name?has_content]

        [#--  If the poll has not started yet.  --]
        [#if .now < targetPoll.startDate]
          <h1 class="page_notification">
            ${i18n['voting-app.templates.voting.notification.notStartedYet.label']}
          </h1>
          <h1 class="page_notification" style="margin-top: 0;">
            ${i18n['voting-app.templates.voting.notification.startTimeIndication.label']}
            ${targetPoll.startDate?string["HH:mm d MMMM yyyy"]!"the start day."}
          </h1>
          
        [#--  If the poll has already finished.  --]
        [#elseif targetPoll.expiryDate <= .now]
          <h1 class="page_notification">
            ${i18n['voting-app.templates.voting.notification.finished.label']}
          </h1>
        
        [#--  If the poll is still in progress.  --]
        [#else]
          [#assign message = ctx.request.getAttribute("message")!]
          [#if message?has_content]
          
            [#--  If the poll is submitted successfully.  --]
            [#if message == "successful"]
              [#assign image = damfn.getAsset(targetPoll.imageLink)!]
              [#--  
                The image of a poll can probably be deleted in DAM 
                despite the fact that the poll is still existing.  
              --]
              [#if image?has_content && image.link?has_content]
                [#assign imagePath = image.link]
              [#else]
                [#--  Default image.  --]
                [#assign imagePath = ctx.contextPath+"/.resources/voting-contentApp/resources/image/default.jpg"]
              [/#if]

              <div class="banner">
                <img class="banner_image" src="${imagePath}">
                <div class="banner_info">
                  <h1 class="banner_title">${targetPoll.@name!"No title."}</h1>
                  <p class="poll_description">${targetPoll.description!"No description."}</p>
                </div> 
              </div>

              <h1 class="page_notification" style="margin-top: 10%;">
                ${i18n['voting-app.templates.voting.notification.thankyou.label']}
              </h1>

            [#--  If errors occur while the poll is being submitted.  --]
            [#else]
              <h1 class="page_notification">
                ${i18n['voting-app.templates.voting.notification.submissionFailed.label']}
              </h1>
            [/#if]

          [#--  If the message has no content.  --]
          [#else]
            <h1 class="page_notification">
              ${i18n['voting-app.templates.voting.notification.notAllowed.label']}
            </h1>
          [/#if]
        [/#if]
        
      [#--  
        For handling the not completely deleted poll issue, 
        will be removed after completing FA-25.
      --]
      [#else]
        <h1 class="page_notification">
          ${i18n['voting-app.templates.voting.notification.deleted.label']}
        </h1>
      [/#if]
    [#else]
      <h1 class="page_notification">
        ${i18n['voting-app.templates.voting.notification.unavailable.label']}
      </h1>
    [/#if]
  [#else] 
    <h1 class="page_notification">
      ${i18n['voting-app.templates.voting.notification.noSelected.label']}
    </h1>
  [/#if]
</div> [#--  End of the parent div.  --]