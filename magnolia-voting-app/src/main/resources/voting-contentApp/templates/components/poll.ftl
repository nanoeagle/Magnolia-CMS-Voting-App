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

        [#--  If the poll has already finished.  --]
        [#if targetPoll.expiryDate <= .now]
          <h1 class="page_notification">
            ${i18n['voting-app.templates.voting.notification.finished.label']}
          </h1>
        
        [#--  If the poll is still in progress.  --]
        [#else]
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
              <p class="poll_description">
                ${i18n['voting-app.templates.voting.expiryDate.label']} 
                ${targetPoll.expiryDate?string["HH:mm d MMMM yyyy"]!"No expiry date."}
              </p>
            </div> 
          </div>

          [#--  targetPoll.questions is the 'questions' parent node.  --]
          [#assign questions = cmsfn.children(targetPoll.questions)!]
          [#if questions?size > 0]
            [#list questions]

              [#--  If the poll has not started yet, the action will be hidden.  --]
              [#if .now < targetPoll.startDate]
                [#assign action = ""]
              [#else]
                [#assign action = "voting/submit-poll"]
              [/#if]
              <form class="poll_main" action="${action}" method="post">
                <input type="hidden" name="pollId" value="${targetPoll.@id}">
                <input type="hidden" name="pollSelector" value="${pollSelector}">

                <input class="voter_info" type="text" name="voterFullName" required 
                  placeholder="${i18n['voting-app.components.poll.voterName.placeholder']}">
                <input class="voter_info" type="email" name="voterEmail" 
                  placeholder="${i18n['voting-app.components.poll.voterEmail.placeholder']}">
                
                <div class="selection">
                  [#items as question]
                    ${question.content!"No question content."}

                    [#--  question.answers is the 'answers' parent node.  --]
                    [#assign answers = cmsfn.children(question.answers)!]
                    [#if answers?size > 0]
                      [#list answers]

                        <input type="hidden" name="questionIds" value="${question.@id}">

                        <div class="poll_answer_set">
                          [#items as answer]
                            [#--  Only the predefined answers are showed.  --]
                            [#if !answer.category?has_content]
                              <input class="poll_answer" type="radio" required
                                name="voterChoice_${question.@id}"
                                onclick="enableDisableTextField('otherRadioBtn_${question.@id}', 'answerTextField_${question.@id}')"
                                value="predifined,${answer.@id}">${answer.answerVal!"No answer value."}<br>  
                            [/#if]
                          [/#items]

                          [#if question.allowOptionOfVoter]
                            <input class="poll_answer" type="radio" required
                              id="otherRadioBtn_${question.@id}"
                              name="voterChoice_${question.@id}"
                              onclick="enableDisableTextField('otherRadioBtn_${question.@id}', 'answerTextField_${question.@id}')"
                              value="other,${question.answers.@id}">${i18n['voting-app.components.poll.optionOther.label']}:
                              
                              <input class="poll_answer_text" type="text" disabled
                                id="answerTextField_${question.@id}"
                                name="voterOwnAnswer_${question.@id}" 
                                placeholder="${i18n['voting-app.components.poll.optionOther.text.placeholder']}"><br>
                          [/#if]
                        </div>

                      [/#list] [#--  End of the answer list.  --]
                    [/#if]
                  [/#items]
                </div> [#--  End of the selection.  --]

                [#--  If the poll has not started yet, the submit button won't be showed.  --]
                [#if .now < targetPoll.startDate]
                  <h1 class="page_notification" style="margin-top: 0;">
                    ${i18n['voting-app.templates.voting.notification.notStartedYet.label']}
                  </h1>
                  <h1 class="page_notification" style="margin-top: 0;">
                    ${i18n['voting-app.templates.voting.notification.startTimeIndication.label']}
                    ${targetPoll.startDate?string["HH:mm d MMMM yyyy"]!"the start day."}
                  </h1>
                [#else]
                  <input class="submit_btn" type="submit" value="${i18n['voting-app.components.poll.submitBtn.label']}">
                [/#if]
              </form>

            [/#list] [#--  End of the question list.  --]
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