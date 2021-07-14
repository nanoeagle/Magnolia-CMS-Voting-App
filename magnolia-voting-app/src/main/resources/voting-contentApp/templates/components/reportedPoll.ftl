[#assign pollSelector = state.getSelector()!]
[#assign targetPoll = cmsfn.contentByPath("/"+pollSelector, "polls")!]
[#assign image = damfn.getAsset(targetPoll.imageLink)!]

[#assign nodes = pollfn.getAllChildren(targetPoll)!]
[#assign votesNumber = pollfn.sumVotesNumber()!]
[#assign root = pollfn.getNumberOfVoteForEachAnswer()!]
[#assign voteDetailList = pollfn.getVoteDetail()!]

<div class="parent">
    [#if pollSelector?has_content]
        [#if targetPoll?has_content]
            [#if image?has_content && image.link?has_content]
                [#assign imagePath = image.link]
            [#else]
            [#--  Default image.  --]
                [#assign imagePath = ctx.contextPath+"/.resources/voting-contentApp/webresources/image/default.jpg"]
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

            <p class="total_vote">total vote
                <span class="total_vote_number">${votesNumber}</span>
            </p>
            <hr>
            [#if votesNumber > 0]
                <div>
                    [#list root?values as child]
                        [#list child as answer, noOfVotes]
                            <ul id="list_vote_numbers">
                                <li class="vote_detail_number">${noOfVotes}</li>
                                <li class="vote_detail_option">${answer}</li>
                            </ul>
                        [/#list]
                    [/#list]
                </div>
                <table class="header_table detail_vote_table">
                    <tr>
                        <td>Full Name</td>
                        <td class="email_column">Email Address</td>
                        <td>Vote</td>
                        <td class="other_column">Other</td>
                    </tr>
                </table>
                <hr>
                <table class="detail_table detail_vote_table">
                    [#list voteDetailList as aVoteDetail]
                        <tr>
                            <td id=>${aVoteDetail[0]}</td>
                            <td class="email_column">${aVoteDetail[1]}</td>
                            <td>${aVoteDetail[2]}</td>
                            <td class="other_column">${aVoteDetail[3]}</td>
                        </tr>
                    [/#list]
                </table>
            [#else]
                <p class="no_submit_message">${i18n['voting-app.templates.pollReport.notification.hasNoSubmit.label']}</p>
            [/#if]

        [#else]
            <h1 class="page_notification">
                ${i18n['voting-app.templates.voting.notification.unavailable.label']}
            </h1>
        [/#if]
    [#else]
        <h1 class="page_notification">
            ${i18n['voting-app.components.poll.notification.noSelected.label']}
        </h1>
    [/#if]
</div> [#--  End of the parent.  --]