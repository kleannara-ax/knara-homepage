# 깨끗한나라(Kleannara) 홈페이지 DB ERD

본 ERD는 `dump-kleannara-202610021032.sql` (구조 전용 MySQL/MariaDB 덤프, 2026-10-02 10:32 추출)
및 소스코드(`src/main/resources/mapper/*.xml`, `src/main/java/com/kleannara/model/*.java`)를
근거로 작성되었습니다.

## 파일 목록

| 파일 | 설명 |
|---|---|
| `kleannara_erd.mmd` | 전체 상세 ERD (14개 테이블, 전체 컬럼/타입/코멘트 포함) Mermaid 소스 |
| `kleannara_erd.png` | 전체 상세 ERD 렌더링 이미지 (5356×6000px) |
| `kleannara_erd.svg` | 전체 상세 ERD 벡터 이미지 (확대해도 깨지지 않음, 웹에서 보기 권장) |
| `kleannara_erd_overview.mmd` | 테이블 간 관계만 보여주는 요약 다이어그램 Mermaid 소스 |
| `kleannara_erd_overview.png` | 요약 다이어그램 렌더링 이미지 |

## 테이블 요약 (13개 실존 + 1개 레거시)

| 테이블 | 용도 | 비고 |
|---|---|---|
| `admin` | 관리자 계정 | 메뉴별 권한 플래그(`auth_menu1~8`) |
| `adminLog` | 관리자 활동 로그 | `admin.id`를 문자열로만 참조 (실제 FK 없음) |
| `banner` | 메인 배너 | PC/태블릿/모바일 파일 분리 |
| `brand` | 브랜드(용지/PS/HL사업부) | 3개 국어 컬럼 |
| `category` | 브랜드 하위 카테고리 | `brand_idx`로 `brand` 참조 |
| `product` | 제품 | `category_idx`로 `category` 참조 |
| `news` | 공지/보도자료/홍보자료 통합 게시판 | `type`으로 구분 |
| `report` | IR/ESG/감사보고서 등 리포트 | 공시지가 연계(`acpt_no`) |
| `faq` | FAQ | |
| `popup` | 팝업 공지 | 좌표/사이즈/기간 관리 |
| `customer_information_log` | 고객 안내(약관/정책) 변경 이력 | 버전관리 |
| `voc` | 부조리신고센터 VOC 원본 | 개인정보 컬럼 포함(PII) |
| `voc_answers` | VOC 답변(DM) | `p_idx`로 `voc` 참조 |
| `voc_file` | VOC 첨부파일 | `p_idx`로 `voc` 참조 |
| `voc_asis` | ⚠️ 코드(Mapper/Model/관리자화면)에는 존재하지만 **이 덤프 DB에는 테이블 자체가 없음**. 레거시 또는 별도 DB 가능성 — 운영 여부 확인 필요 |

## 관계(FK) 관련 주의사항

- 덤프에는 **실제 FOREIGN KEY 제약조건이 전혀 없습니다.** 모든 관계는 애플리케이션(MyBatis Mapper) 코드 수준에서 `idx` 값을 수동으로 맞춰 쓰는 "논리적 FK"입니다.
  - `brand.idx` ← `category.brand_idx`
  - `category.idx` ← `product.category_idx`
  - `voc.idx` ← `voc_answers.p_idx`, `voc_file.p_idx`
  - `admin.idx`류 작성자 컬럼(`reg_admin`, `reg_member`, `red_member` 등)도 전부 비FK 정수 컬럼
- 따라서 DB 레벨에서 참조 무결성이 보장되지 않으며, 삭제/수정 시 애플리케이션 로직(또는 배치 프로시저)이 직접 cascade 처리를 담당합니다.
  - 예: `proc_update_voc_masking` 프로시저가 `voc_file`을 `voc`와 JOIN해서 수동으로 cascade delete 수행.

## 저장 프로시저 (3개)

| 이름 | 기능 |
|---|---|
| `proc_update_admin_allow` | 90일 이상 미로그인 관리자의 `login_allow`를 1로 자동 변경 |
| `proc_update_admin_psw` | 비밀번호 변경 플래그(`passwordChangeFlg`) 90일 경과 시 리셋 |
| `proc_update_voc_masking` | VOC 3년 경과 데이터 개인정보 마스킹 + 첨부파일 삭제 (개인정보보호법 대응 배치) |

## 생성 방법 (재생성 시 참고)

```bash
npx -y @mermaid-js/mermaid-cli -i kleannara_erd.mmd -o kleannara_erd.png -b white -s 2 --size 3000
npx -y @mermaid-js/mermaid-cli -i kleannara_erd.mmd -o kleannara_erd.svg -b white
```
