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
            <h1 class="banner_title">${targetPoll.@name}</h1>
            <p class="poll_description">${targetPoll.description}</p>
            <p class="poll_description">
              ${i18n['voting-app.templates.voting.expiryDate.label']} 
              ${targetPoll.expiryDate?string["HH:mm d MMMM yyyy"]}
            </p>
          </div> 
        </div>

        [#--  targetPoll.questions is the 'questions' parent node.  --]
        [#assign questions = cmsfn.children(targetPoll.questions)!]
        [#if questions?size > 0]
          [#list questions]
            <form class="poll_main" action="submitPoll.do" method="post">
              <input type="hidden" name="pollId" value="${targetPoll.@id}">

              <input class="voter_info" type="text" name="voterFullName" required 
                placeholder="${i18n['voting-app.components.poll.voterName.placeholder']}">
              <input class="voter_info" type="email" name="voterEmail" 
                placeholder="${i18n['voting-app.components.poll.voterEmail.placeholder']}">
              
              <div class="selection">
                [#items as question]
                  ${question.content}

                  [#--  question.answers is the 'answers' parent node.  --]
                  [#assign answers = cmsfn.children(question.answers)!]
                  [#if answers?size > 0]
                    [#list answers]
                      <div class="poll_answer_set">
                        [#items as answer]
                          <input class="poll_answer" type="radio" value="${answer.answerVal}" required
                            name="voterChoice-${question_index}">${answer.answerVal}<br>
                        [/#items]
                        [#if question.allowOptionOfVoter]
                          <input class="poll_answer" type="radio" required
                            value="${i18n['voting-app.components.poll.optionOther.label']}" 
                            name="voterChoice-${question_index}">${i18n['voting-app.components.poll.optionOther.label']}:
                            
                            <input class="poll_answer_text" type="text" name="voterOwnAnswer-${question_index}" 
                              placeholder="${i18n['voting-app.components.poll.optionOther.text.placeholder']}"><br>
                        [/#if]
                      </div>
                    [/#list] [#--  End of the answer list.  --]
                  [/#if]
                [/#items]
              </div> [#--  End of the selection.  --]

              [#--  submit button is enabled if there is at least a question.  --]
              <input class="submit_btn" type="submit" value="${i18n['voting-app.components.poll.submitBtn.label']}">
            </form>
          [/#list] [#--  End of the question list.  --]
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
</div> [#--  End of the parent.  --]