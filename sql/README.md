# SQL / DB 스키마 자료

## 폴더 구조

| 경로 | 설명 |
|---|---|
| `schema/dump-kleannara-202610021032.sql` | `kleannara` DB 구조 전용 덤프 (2026-10-02 10:32 추출, 호스트 `10.41.84.94`). **테이블 구조(DDL)와 저장 프로시저만 포함되며 실제 데이터(행)는 포함되지 않음.** |

## 포함된 내용
- 테이블 13개: `admin`, `adminLog`, `banner`, `brand`, `category`, `customer_information_log`,
  `faq`, `news`, `popup`, `product`, `report`, `voc`, `voc_answers`, `voc_file`
- 저장 프로시저 3개: `proc_update_admin_allow`, `proc_update_admin_psw`, `proc_update_voc_masking`

테이블 상세 설명과 관계(ERD)는 [`../docs/erd/README.md`](../docs/erd/README.md) 참고.

## ⚠️ 주의사항
- 이 덤프는 **구조 전용(`--no-data` 방식)**이라 개인정보/실 데이터가 없습니다. 하지만 향후 데이터가 포함된 덤프를 올릴 경우,
  `voc` 테이블 등 PII(이름/이메일/전화번호/주소 등)가 포함된 테이블은 **절대 평문으로 커밋하지 마세요.**
- 코드(`VocAsisMapper.xml`)에서 참조하는 `voc_asis` 테이블은 이 덤프에 존재하지 않습니다. 레거시 테이블이거나 별도 DB에 있을 가능성이 있어 별도 확인이 필요합니다.
