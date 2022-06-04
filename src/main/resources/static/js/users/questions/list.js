async function question_status_update(question_url, status_url) {
    return axios({
        url: question_url,
        method: "PATCH",
        data: JSON.stringify({ practiceStatus: status_url }),
        withCredentials: true,
        headers: { "Content-Type": `application/json`}
    });
}

async function question_status_update_handler(select_dom, question, old_status, new_status) {
    if (old_status != new_status) {
        try {
            await question_status_update(question, new_status);
            select_dom[0].setAttribute("data-status", new_status);
            alertify.success('업데이트 성공');
        } catch (err) {
            alertify.error('업데이트 실패! 잠시후 시도하세요');
            select_dom.selectpicker('val', old_status);
        }
    }
}

function reload_questions(questions_url, status, title) {
    questions_url = new URL(questions_url);

    if(title != "")
        questions_url.searchParams.append("title", title);
    if(status != 0)
        questions_url.searchParams.append("practiceStatus", status);
    window.location.href=questions_url;
}

/*
 * handlers
 */
document.addEventListener("DOMContentLoaded", function() {    // Handler when the DOM is fully loaded});
    $('.users.questions .datas select').on('changed.bs.select', async function (e, clickedIndex, isSelected, oldValue) {
        // selectpicker 메소드를 통해 조작시 clickedIndex와 isSelected는 null 이 된다.
        if (clickedIndex != null) {
            /*
             * select dom
             */
            const select_dom = $(this); // selectpicker

            /*
             * datas
             */
            const question = select_dom[0].getAttribute("data-question");
            const origin = select_dom[0].getAttribute("data-status");
            const updated = select_dom.val(); //클릭된 새로운 값
            await question_status_update_handler(select_dom, question, origin, updated);
        }
    })

    /*
     * search handler
     */
    // 상태 필터를 걸었을때
    $('.users.questions.search select').on('changed.bs.select', async function (e, clickedIndex, isSelected, oldValue) {
        // selectpicker 메소드를 통해 조작시 clickedIndex와 isSelected는 null 이 된다.
        if (clickedIndex != null) {
            const status = $(this).val(); // selectpicker
            const questions_url = document
                .getElementsByClassName("users questions root")[0]
                .getAttribute("data-questions-url");
            const title = document
                .querySelector(".users.questions.search > .form > .form-control").value;

            reload_questions(questions_url, status, title);
        }
        $(".users.questions.search .selectpicker").val()
    })

    // 검색 버튼을 클릭했을때
    $('.users.questions.search > .form > button').on('click', function () {
        const status = $(".users.questions.search .selectpicker").val(); // selectpicker
        const questions_url = document
            .getElementsByClassName("users questions root")[0]
            .getAttribute("data-questions-url");
        const title = document
            .querySelector(".users.questions.search > .form > .form-control").value;

        reload_questions(questions_url, status, title);
    });

    // 검색어를 입력한후 엔터를 쳣을때
    $('.users.questions.search > .form > input').on('keypress', function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.querySelector(".users.questions.search > .form > button").click();
        }
    });
});