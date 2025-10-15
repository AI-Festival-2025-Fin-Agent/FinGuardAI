/**
 * 공시 페이지 - 최근 7일간 날짜별 공시 조회
 */

document.addEventListener('DOMContentLoaded', function() {
    'use strict';

    const disclosureView = document.getElementById('view-disclosure');
    if (!disclosureView) return;

    let currentDate = null;
    let disclosureSummary = {};

    // CSS 추가
    const style = document.createElement('style');
    style.textContent = `
        .date-calendar {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 10px;
            margin: 20px 0;
        }
        .date-card {
            background: var(--panel);
            border: 2px solid var(--line);
            border-radius: 12px;
            padding: 16px;
            text-align: center;
            cursor: pointer;
            transition: all 0.2s;
            min-width: 90px;
        }
        .date-card:hover {
            border-color: var(--acc);
            transform: translateY(-2px);
        }
        .date-card.active {
            background: var(--acc);
            border-color: var(--acc);
            color: white;
        }
        .date-label {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 8px;
            white-space: nowrap;
        }
        .date-count {
            font-size: 20px;
            font-weight: 700;
        }

        /* 모바일: 가로 스크롤 */
        @media (max-width: 768px) {
            .date-calendar {
                display: flex;
                overflow-x: auto;
                scroll-snap-type: x mandatory;
                -webkit-overflow-scrolling: touch;
                gap: 12px;
                padding-bottom: 10px;
            }
            .date-card {
                flex: 0 0 auto;
                width: 90px;
                scroll-snap-align: start;
            }
            /* 스크롤바 스타일링 */
            .date-calendar::-webkit-scrollbar {
                height: 6px;
            }
            .date-calendar::-webkit-scrollbar-track {
                background: #f1f1f1;
                border-radius: 10px;
            }
            .date-calendar::-webkit-scrollbar-thumb {
                background: var(--acc);
                border-radius: 10px;
            }
        }
        .disclosure-list-container {
            margin-top: 20px;
        }
        .disclosure-item {
            background: var(--panel);
            border: 1px solid var(--line);
            border-radius: 12px;
            padding: 16px;
            margin-bottom: 12px;
            cursor: pointer;
            transition: all 0.2s;
        }
        .disclosure-item:hover {
            border-color: var(--acc);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .disclosure-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;
        }
        .disclosure-company {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 4px;
        }
        .disclosure-report {
            font-size: 14px;
            color: var(--muted);
            margin-bottom: 8px;
        }
        .disclosure-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
        }
        .disclosure-submitter {
            color: var(--muted);
        }
        .link-btn {
            background: none;
            border: none;
            color: var(--acc);
            cursor: pointer;
            font-weight: 600;
        }
        .modal {
            border: none;
            border-radius: 16px;
            padding: 0;
            max-width: 800px;
            width: 90%;
            max-height: 80vh;
        }
        .modal-content {
            padding: 24px;
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 16px;
            border-bottom: 1px solid var(--line);
        }
        .modal-body {
            max-height: 60vh;
            overflow-y: auto;
        }
        .disclosure-text pre {
            white-space: pre-wrap;
            font-size: 14px;
            line-height: 1.6;
            max-height: 400px;
            overflow-y: auto;
            background: #f5f5f5;
            padding: 16px;
            border-radius: 8px;
        }
        .loading, .error {
            text-align: center;
            padding: 40px;
            color: var(--muted);
        }
    `;
    document.head.appendChild(style);

    /**
     * 페이지 초기화
     */
    function initDisclosurePage() {
        console.log('[Disclosure] 페이지 초기화');
        loadDisclosureSummary();
    }

    /**
     * 최근 7일간 공시 요약 데이터 로드
     */
    async function loadDisclosureSummary() {
        try {
            const response = await fetch('/api/disclosures/summary');
            if (!response.ok) throw new Error('Summary API 오류');

            disclosureSummary = await response.json();
            console.log('[Disclosure] 요약 데이터:', disclosureSummary);

            renderDateCalendar();
        } catch (error) {
            console.error('[Disclosure] 요약 데이터 로드 실패:', error);
            disclosureView.innerHTML = '<div class="section"><div class="panel"><p class="error">공시 데이터를 불러오는데 실패했습니다.</p></div></div>';
        }
    }

    /**
     * 날짜 캘린더 렌더링
     */
    function renderDateCalendar() {
        // DB에 있는 실제 날짜 기준으로 정렬 (최신순)
        const dateStrings = Object.keys(disclosureSummary).sort().reverse();

        if (dateStrings.length === 0) {
            disclosureView.innerHTML = '<div class="section"><div class="panel"><p class="muted center">표시할 공시 데이터가 없습니다.</p></div></div>';
            return;
        }

        const calendarHTML = `
            <div class="section">
                <div class="panel">
                    <h2 class="panel-title">📅 날짜별 공시</h2>
                    <p class="muted">날짜를 클릭하면 해당 날짜의 공시 목록을 확인할 수 있습니다.</p>
                    <div class="date-calendar">
                        ${dateStrings.map((dateStr, index) => {
                            const count = disclosureSummary[dateStr] || 0;
                            const date = new Date(dateStr + 'T00:00:00'); // 로컬 타임존으로 파싱
                            const isFirst = index === 0; // 첫 번째(최신) 날짜를 기본 선택

                            return `
                                <div class="date-card ${isFirst ? 'active' : ''}" data-date="${dateStr}">
                                    <div class="date-label">${formatDateLabel(date)}</div>
                                    <div class="date-count">${count}건</div>
                                </div>
                            `;
                        }).join('')}
                    </div>
                </div>
            </div>
            <div class="disclosure-list-container" id="disclosure-list-container"></div>
        `;

        disclosureView.innerHTML = calendarHTML;

        // 날짜 카드 클릭 이벤트
        document.querySelectorAll('.date-card').forEach(card => {
            card.addEventListener('click', () => {
                const date = card.dataset.date;
                selectDate(date);
            });
        });

        // 최신 날짜 자동 선택
        if (dateStrings.length > 0) {
            selectDate(dateStrings[0]);
        }
    }

    /**
     * 날짜 선택
     */
    async function selectDate(dateStr) {
        currentDate = dateStr;

        document.querySelectorAll('.date-card').forEach(card => {
            card.classList.remove('active');
        });

        const selectedCard = document.querySelector(`.date-card[data-date="${dateStr}"]`);
        if (selectedCard) {
            selectedCard.classList.add('active');
        }

        await loadDisclosureList(dateStr);
    }

    /**
     * 특정 날짜의 공시 목록 로드
     */
    async function loadDisclosureList(dateStr) {
        const container = document.getElementById('disclosure-list-container');
        container.innerHTML = '<div class="loading">공시 목록을 불러오는 중...</div>';

        try {
            const response = await fetch(`/api/disclosures?date=${dateStr}`);
            if (!response.ok) throw new Error('Disclosures API 오류');

            const disclosures = await response.json();
            console.log(`[Disclosure] ${dateStr} 공시:`, disclosures);

            renderDisclosureList(disclosures, dateStr);
        } catch (error) {
            console.error('[Disclosure] 공시 목록 로드 실패:', error);
            container.innerHTML = '<div class="error">공시 목록을 불러오는데 실패했습니다.</div>';
        }
    }

    /**
     * 공시 목록 렌더링
     */
    function renderDisclosureList(disclosures, dateStr) {
        const container = document.getElementById('disclosure-list-container');

        if (disclosures.length === 0) {
            container.innerHTML = `
                <div class="section">
                    <div class="panel">
                        <p class="muted center">해당 날짜에 공시가 없습니다.</p>
                    </div>
                </div>
            `;
            return;
        }

        const listHTML = `
            <div class="section">
                <div class="panel">
                    <h3 class="panel-subtitle">${formatDateKorean(dateStr)} 공시 목록 (${disclosures.length}건)</h3>
                    <div class="list">
                        ${disclosures.map(disclosure => `
                            <div class="disclosure-item" data-rcept-no="${disclosure.rceptNo}">
                                <div class="disclosure-header">
                                    <span class="chip ${disclosure.isCorrection ? 'warn' : 'ghost'}">
                                        ${disclosure.isCorrection ? '정정' : disclosure.market || '일반'}
                                    </span>
                                    <span class="time">${disclosure.disclosureTime || ''}</span>
                                </div>
                                <div class="disclosure-company">${disclosure.company}</div>
                                <div class="disclosure-report">${disclosure.report}</div>
                                <div class="disclosure-footer">
                                    <span class="disclosure-submitter">${disclosure.submitter}</span>
                                    <button class="link-btn" data-rcept-no="${disclosure.rceptNo}">상세보기 →</button>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        `;

        container.innerHTML = listHTML;

        // 상세보기 버튼
        container.querySelectorAll('.link-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                showDisclosureDetail(btn.dataset.rceptNo);
            });
        });

        // 공시 아이템 클릭
        container.querySelectorAll('.disclosure-item').forEach(item => {
            item.addEventListener('click', () => {
                showDisclosureDetail(item.dataset.rceptNo);
            });
        });
    }

    /**
     * 공시 상세 모달
     */
    async function showDisclosureDetail(rceptNo) {
        console.log('[Disclosure] 상세 조회:', rceptNo);

        const modal = document.createElement('dialog');
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h2>공시 내용</h2>
                    <button class="icon-btn close-modal">
                        <svg height="22" width="22" viewBox="0 0 24 24">
                            <path d="M6 6l12 12M6 18L18 6" stroke="currentColor" stroke-width="2"/>
                        </svg>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="loading">공시 내용을 불러오는 중...</div>
                </div>
            </div>
        `;

        document.body.appendChild(modal);
        modal.showModal();

        modal.querySelector('.close-modal').addEventListener('click', () => {
            modal.close();
            modal.remove();
        });

        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.close();
                modal.remove();
            }
        });

        try {
            const response = await fetch(`/api/disclosures/${rceptNo}/content`);
            if (!response.ok) throw new Error('Content API 오류');

            const contentData = await response.json();

            modal.querySelector('.modal-body').innerHTML = `
                <div class="disclosure-content">
                    <div class="disclosure-meta">
                        <h3>${contentData.company}</h3>
                        <p class="muted">${contentData.report}</p>
                        <p class="muted">접수번호: ${contentData.rceptNo}</p>
                    </div>
                    <div class="disclosure-text">
                        <pre>${contentData.content}</pre>
                    </div>
                    <div style="margin-top: 20px;">
                        <a href="https://dart.fss.or.kr/dsaf001/main.do?rcpNo=${contentData.rceptNo}"
                           target="_blank" class="cta">
                            DART에서 원문 보기 ↗
                        </a>
                    </div>
                </div>
            `;
        } catch (error) {
            console.error('[Disclosure] 내용 로드 실패:', error);
            modal.querySelector('.modal-body').innerHTML = '<div class="error">공시 내용을 불러오는데 실패했습니다.</div>';
        }
    }

    // === 유틸리티 함수 ===
    function formatDateForAPI(date) {
        // 로컬 타임존 기준으로 YYYY-MM-DD 형식 반환
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    function formatDateLabel(date) {
        const days = ['일', '월', '화', '수', '목', '금', '토'];
        return `${date.getMonth() + 1}/${date.getDate()} (${days[date.getDay()]})`;
    }

    function formatDateKorean(dateStr) {
        const date = new Date(dateStr);
        const days = ['일', '월', '화', '수', '목', '금', '토'];
        return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일 (${days[date.getDay()]})`;
    }

    function isDateToday(date) {
        const today = new Date();
        return date.toDateString() === today.toDateString();
    }

    // === 뷰 전환 감지 ===
    const observer = new MutationObserver(() => {
        if (disclosureView.classList.contains('active') && !disclosureView.querySelector('.date-calendar')) {
            initDisclosurePage();
        }
    });
    observer.observe(disclosureView, { attributes: true, attributeFilter: ['class'] });

    // 해시 변경 감지
    window.addEventListener('hashchange', () => {
        if (location.hash === '#disclosure' && disclosureView.classList.contains('active')) {
            initDisclosurePage();
        }
    });

    // 초기 로드
    if (disclosureView.classList.contains('active')) {
        initDisclosurePage();
    }
});
