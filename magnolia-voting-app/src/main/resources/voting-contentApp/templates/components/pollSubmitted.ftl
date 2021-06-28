[#assign pollSelector = state.getSelector()!]
[#assign targetPoll = cmsfn.contentByPath("/"+pollSelector, "polls")!]
[#assign image = damfn.getAsset(targetPoll.imageLink)!]

<div class="parent">
    [#if pollSelector?has_content]
        [#if targetPoll?has_content]
            [#if targetPoll.name?has_content]
                [#if image?has_content && image.link?has_content]
                    [#assign imagePath = image.link]
                [#else]
                [#--  Default image.  --]
                    [#assign imagePath = ctx.contextPath+"/.resources/voting-contentApp/webresources/image/default.jpg"]
                [/#if]
                <div class="banner">
                    <img class="banner_image" src="${imagePath}">
                    <div class="banner_info">
                        <h1 class="banner_title">${targetPoll.name}</h1>
                        <p class="poll_description">${targetPoll.description}</p>
                    </div>
                </div>
                <p class="thanks_message">${i18n['voting-app.components.pollSubmitted.thankyou.label']}</p>

            [#--      For handling the not completely deleted poll issue.--]
            [#else]
                ${i18n['voting-app.templates.voting.notification.deleted.label']}
            [/#if]

        [#else]
            ${i18n['voting-app.templates.voting.notification.unavailable.label']}
        [/#if]

    [#else]
        ${i18n['voting-app.templates.voting.notification.noSelected.label']}
    [/#if]
</div>