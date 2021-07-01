<div class="parent">
    [#assign pollSelector = state.getSelector()!]
    [#if pollSelector?has_content]

        [#assign targetPoll = cmsfn.contentByPath("/"+pollSelector, "polls")!]
        [#if targetPoll?has_content]


            [#if targetPoll.name?has_content]
                [#assign image = damfn.getAsset(targetPoll.imageLink)!]

                [#if image?has_content && image.link?has_content]
                    [#assign imagePath = image.link]
                [#else]
                [#--  Default image.  --]
                    [#assign imagePath = ctx.contextPath+"/.resources/voting-contentApp/resources/image/default.jpg"]
                [/#if]

                <div class="banner">
                    <img class="banner_image" src="${imagePath}">
                    <div class="banner_info">
                        <h1 class="banner_title">${targetPoll.name}</h1>
                        <p class="poll_description">${targetPoll.description}</p>
                        <p class="poll_description">
                            ${i18n['voting-app.components.poll.expiryDate.label']}
                            ${targetPoll.expiryDate?string["HH:mm d MMMM yyyy"]}
                        </p>
                    </div>
                </div>

            [#--                My code starts here--]


            [#--  targetPoll.questions is the 'questions' parent node.  --]
                [#assign questions = cmsfn.children(targetPoll.questions)!]
                [#if questions?size > 0]
                    [#list questions]
                        <div class="selection">
                            [#items as question]
                            [#--                                    answerNode.getChildrent("voterType").count()--]
                                [#assign answers = cmsfn.children(question.answers)!]

                                <label style="text-transform: uppercase">total vote</label>
                                <hr style="height: 1.2px; background-color: black; border: none">

[#--                                [#assign votersNo = cmsfn.children(answers.voters).count()!]--]
[#--                                ${votersNo}--]
                                [#if answers?size > 0]
                                    [#list answers]
                                        <table>
                                            [#items as answer]
                                                [#assign voters = cmsfn.children(answer.voters)!]
                                                <tr>
                                                    <td class="report_td">${voters?size}</td>
                                                    <td class="report_td">${answer.value}</td>
                                                </tr>
                                            [/#items]
                                            [#if question.allowOptionOfVoter]
                                                <td class="report_td">Other</td>
                                                <td class="report_td">Other</td>
                                            [/#if]
                                        </table>
                                    [/#list] [#--  End of the answer list.  --]
                                    ${myList}
                                [#else]
                                    <h3 class="answer_notification">
                                        ${i18n['voting-app.components.poll.notification.noOption.label']}
                                    </h3>
                                [/#if]
                            [/#items]
                        </div> [#--  End of the selection.  --]

                    [#--  submit button is enabled if there is at least a question.  --]
                        <input class="submit_btn" type="submit"
                               value="${i18n['voting-app.templates.voting.submitBtn.label']}">
                    [/#list] [#--  End of the question list.  --]
                [#else]

                    <h1 class="question_notification">
                        ${i18n['voting-app.components.poll.notification.noQuestion.label']}
                    </h1>
                [/#if]

            [#else]
                <h1 class="page_notification">
                    ${i18n['voting-app.components.poll.notification.deleted.label']}
                </h1>
            [/#if]
        [#else]
            <h1 class="page_notification">
                ${i18n['voting-app.components.poll.notification.unavailable.label']}
            </h1>
        [/#if]
    [#else]
        <h1 class="page_notification">
            ${i18n['voting-app.components.poll.notification.noSelected.label']}
        </h1>
    [/#if]
</div> [#--  End of the parent.  --]