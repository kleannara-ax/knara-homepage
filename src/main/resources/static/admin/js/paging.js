// 쿼리 스트링 파라미터 셋팅
function setQueryStringParams() {
    if (!location.search) {
        return false;
    }

    const form = document.getElementById('searchForm');

    new URLSearchParams(location.search).forEach((value, key) => {
        if (form[key]) {
            form[key].value = value;
        }
    })

    document.getElementById('mainKeyword').value = form.keyword.value;
}

// 페이지 HTML draw
function drawPage(pagination, params) {
    if(pagination.totalRecordCount == 0)
        return;

    if (!pagination || !params) {
        document.querySelector('.page-nav').innerHTML = '';
        return false;
    }
    let html = '';
    // 첫 페이지, 이전 페이지
    if (pagination.existPrevPage) {
        html += `
                        <a href="javascript:void(0)" onclick="movePage(1);" class="first"></a>
                        <a href="javascript:void(0)" onclick="movePage(${pagination.startPage - 1});" class="prev"></a>
                    `;
    }

    // 페이지 번호
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        if(i === params.page)
            html += `<span>${i}</span>`;
        else
            html += `<a href="javascript:void(0)" onclick="movePage(${i})">${i}</a>`;
    }

    // 다음 페이지, 마지막 페이지
    if (pagination.existNextPage) {
        html += `
                        <a href="javascript:void(0)" onclick="movePage(${pagination.endPage + 1});" class="next"></a>
                        <a href="javascript:void(0)" onclick="movePage(${pagination.totalPageCount});" class="last"></a>
                    `;
    }

    document.querySelector('.page-nav').innerHTML = html;
}

// 페이지이동
function movePage(page) {

    const form = document.getElementById('searchForm');
    let queryParams = {
        page: (page) ? page : 1,
        recordSize: 10,
        pageSize: 10,
        keyword: form.keyword.value,
        searchType: form.searchType.value
    }
    if(form.req_type)
        queryParams['req_type'] = form.req_type.value;
    if(form.status)
        queryParams['status'] = form.status.value;
    if(form.sub_type)
        queryParams['sub_type'] = form.sub_type.value;
    if(form.type)
        queryParams['type'] = form.type.value;
    if(form.searchType)
        queryParams['searchType'] = form.searchType.value;
    if(form.vocCategoryCode)
        queryParams['vocCategoryCode'] = form.vocCategoryCode.value;
    if(form.vocStatus)
        queryParams['vocStatus'] = form.vocStatus.value;

    location.href = location.pathname + '?' + new URLSearchParams(queryParams).toString();
}