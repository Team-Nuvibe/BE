package com.umc.nuvibe.domain.image.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ImageTag {
    //=======Mood
    BLUR(
            ImageTagCategory.MOOD,
            "블러",
            List.of("흐린", "번지는", "포커스아웃"),
            "또렷하지 않아 더 감각적인 상태를 담은 태그입니다. 초점이 흐려진 사진, 번지는 빛이나 움직임이 남은 순간을 올려보세요."
    ),
    GRAIN(
            ImageTagCategory.MOOD,
            "그레인",
            List.of("거친","필름", "입자"),
            "거칠고 입자감 있는 질감을 표현하는 감각입니다. 필름 사진, 노이즈가 살아 있는 이미지로 분위기를 남겨보세요."
    ),
    SILENCE(
            ImageTagCategory.MOOD,
            "정적",
            List.of("조용함", "고요", "차분한"),
            "소리가 사라진 듯한 고요한 순간을 의미합니다. 사람 없는 공간, 멈춘 장면, 조용한 풍경을 기록해보세요."
    ),
    CALM(
            ImageTagCategory.MOOD,
            "차분",
            List.of("잔잔한", "안정", "편안한"),
            "마음이 가라앉은 안정적인 상태를 담는 태그입니다. 잔잔한 색감, 정돈된 공간, 평온한 일상을 올려보세요."
    ),
    STILL(
            ImageTagCategory.MOOD,
            "멈춤",
            List.of("정지", "고요", "차분한"),
            "시간이나 움직임이 잠시 멈춘 느낌을 표현합니다. 정지된 오브제, 가만히 머문 순간을 이미지로 남겨보세요."
    ),
    SLOW(
            ImageTagCategory.MOOD,
            "느림",
            List.of("천천히", "여유", "슬로우"),
            "빠르지 않아 더 깊이 느껴지는 감각입니다. 여유 있는 하루, 천천히 흘러가는 장면을 기록해보세요."
    ),
    LOVELY(
            ImageTagCategory.MOOD,
            "사랑스러운",
            List.of("애정", "사랑하는", "달콤한"),
            "부드럽고 따뜻한 감정을 담은 태그입니다. 작은 소품, 미소, 애정이 느껴지는 순간을 올려보세요."
    ),
    HEAVY(
            ImageTagCategory.MOOD,
            "무거움",
            List.of("묵직한", "진함", "딥"),
            "묵직하고 깊은 분위기를 표현하는 감각입니다. 어두운 톤, 밀도 있는 색감, 강한 인상을 담아보세요."
    ),
    LIGHT(
            ImageTagCategory.MOOD,
            "가벼움",
            List.of("산뜻", "밝음", "라이트"),
            "산뜻하고 부담 없는 분위기를 의미합니다. 밝은 빛, 공기감 있는 사진으로 기분을 표현해보세요."
    ),
    SOFT(
            ImageTagCategory.MOOD,
            "부드러움",
            List.of("말랑", "소프트", "은은"),
            "자극 없이 은은하게 스며드는 감각입니다. 말랑한 질감, 흐린 경계, 포근한 이미지를 올려보세요."
    ),
    RAW(
            ImageTagCategory.MOOD,
            "날것",
            List.of("거친", "가공안된", "자연"),
            "가공되지 않은 솔직한 상태를 담는 태그입니다. 꾸미지 않은 공간, 자연스러운 순간을 그대로 기록해보세요."
    ),
    WARM(
            ImageTagCategory.MOOD,
            "따뜻",
            List.of("온기", "포근", "웜"),
            "온기와 포근함이 느껴지는 감각입니다. 햇살, 조명, 사람의 기운이 담긴 장면을 추천해요."
    ),
    COOL(
            ImageTagCategory.MOOD,
            "차가움",
            List.of("쿨", "시원", "냉"),
            "시원하고 거리감 있는 분위기를 표현합니다. 차가운 색감, 금속, 맑은 공기가 느껴지는 이미지를 올려보세요."
    ),
    DEEP(
            ImageTagCategory.MOOD,
            "깊음",
            List.of("진함", "딥", "농도"),
            "감정이나 색감이 진하게 스며든 상태입니다. 어두운 톤, 집중된 시선, 깊은 분위기의 사진을 담아보세요."
    ),
    MUTED(
            ImageTagCategory.MOOD,
            "절제",
            List.of("톤다운", "차분", "낮춤"),
            "소리를 낮춘 듯 차분하게 정리된 감각입니다. 톤 다운된 색감, 과하지 않은 장면을 기록해보세요."
    ),

    //=======Light
    SHADOW(
            ImageTagCategory.LIGHT,
            "그림자",
            List.of("음영", "쉐도우", "대비"),
            "빛이 닿지 않은 부분이 만들어내는 형태와 대비를 담는 태그입니다. 그림자가 드리워진 벽, 바닥, 인물의 윤곽 같은 장면을 올려보세요."
    ),
    GLOW(
            ImageTagCategory.LIGHT,
            "빛",
            List.of("은은", "빛나는", "글로우"),
            "은은하게 퍼지며 공간을 감싸는 빛의 느낌을 표현합니다. 조명, 반사된 빛, 부드럽게 번지는 광원을 담아보세요."
    ),
    DUSK(
            ImageTagCategory.LIGHT,
            "노을",
            List.of("해질녘", "저녁", "하늘"),
            "낮과 밤의 경계에 있는 시간대의 색과 공기를 담습니다. 해가 지는 하늘, 붉고 보랏빛이 섞인 풍경을 기록해보세요."
    ),
    DAWN(
            ImageTagCategory.LIGHT,
            "새벽",
            List.of("이른아침", "안개", "하늘"),
            "하루가 시작되기 직전의 차갑고 맑은 빛을 의미합니다. 아직 사람이 없는 거리, 옅은 하늘빛을 담은 이미지를 추천해요."
    ),
    NOON(
            ImageTagCategory.LIGHT,
            "한낮",
            List.of("낮", "정오"),
            "가장 직선적이고 강한 빛이 드러나는 시간의 느낌입니다. 그늘 없는 풍경, 선명한 색감이 살아 있는 장면을 올려보세요."
    ),
    NIGHT(
            ImageTagCategory.LIGHT,
            "밤",
            List.of("야간", "다크"),
            "빛이 최소화된 상태에서 드러나는 분위기를 담습니다. 가로등, 어두운 실내, 밤공기의 밀도를 느낄 수 있는 이미지를 남겨보세요."
    ),
    MORNING(
            ImageTagCategory.LIGHT,
            "아침",
            List.of("오전", "모닝"),
        "부드럽고 투명한 빛이 하루를 여는 순간을 표현합니다. 햇살이 스며드는 창가, 차분한 오전의 장면이 잘 어울립니다."
    ),
    EVENING(
            ImageTagCategory.LIGHT,
            "저녁",
            List.of("노을", "석양"),
            "하루가 정리되는 시간대의 안정적인 빛을 담습니다. 노을 이후의 하늘, 실내 조명이 켜진 공간을 기록해보세요."
    ),
    DARK(
            ImageTagCategory.LIGHT,
            "어두움",
            List.of("다크", "어두운"),
            "빛이 줄어들며 형태와 분위기가 강조되는 상태를 의미합니다. 명암이 강한 장면, 어둠 속에서 드러나는 요소를 담아보세요."
    ),

    BRIGHT(
            ImageTagCategory.LIGHT,
            "밝음",
            List.of("환함", "라이트"),
            "공간을 가득 채운 빛과 개방적인 분위기를 표현합니다. 밝은 낮 풍경, 흰 배경, 환한 실내 이미지를 올려보세요."
    ),

    //=======Color
    BLACK(
            ImageTagCategory.COLOR,
            "블랙",
            List.of("검정", "다크", "어두운"),
            "빛을 흡수하는 깊고 단단한 색감의 인상을 담는 태그입니다. 어두운 배경, 실루엣 중심의 이미지나 강한 대비의 장면을 올려보세요."
    ),
    WHITE(
            ImageTagCategory.COLOR,
            "화이트",
            List.of("흰색", "밝은"),
            "비워진 공간처럼 깨끗하고 여백이 느껴지는 색감을 의미합니다. 밝은 배경, 단순한 구도, 공기감 있는 이미지를 추천해요."
    ),
    GRAY(
            ImageTagCategory.COLOR,
            "그레이",
            List.of("회색", "무채색"),
            "명확하지 않아 더 차분한 중간 톤의 색감을 담습니다. 무채색 공간, 흐린 날씨, 절제된 분위기의 사진을 올려보세요."
    ),
    BEIGE(
            ImageTagCategory.COLOR,
            "베이지",
            List.of("내추럴", "뉴트럴"),
            "따뜻하면서도 튀지 않는 내추럴한 색감을 표현합니다. 자연광, 패브릭, 편안한 일상의 장면이 잘 어울립니다."
    ),
    IVORY(
            ImageTagCategory.COLOR,
            "아이보리",
            List.of("크림", "연한"),
            "화이트보다 부드럽고 온기가 느껴지는 색의 결을 담습니다. 연한 조명, 크림톤 오브제, 포근한 이미지를 기록해보세요."
    ),
    OLIVE(
            ImageTagCategory.COLOR,
            "올리브",
            List.of("카키", "그린"),
            "자연과 가까운 차분한 그린 계열의 색감을 의미합니다. 식물, 빈티지한 공간, 무드 있는 착장 사진을 올려보세요."
    ),
    BURGUNDY(
            ImageTagCategory.COLOR,
            "버건디",
            List.of("와인", "레드"),
            "깊고 농도 있는 붉은 계열의 무드를 담는 태그입니다. 어두운 레드 톤, 묵직한 분위기의 공간이나 오브제를 추천해요."
    ),
    BROWN(
            ImageTagCategory.COLOR,
            "브라운",
            List.of("우드", "갈색"),
            "흙과 나무를 떠올리게 하는 안정적인 색감을 표현합니다. 우드 소재, 빈티지한 소품, 따뜻한 실내 장면을 올려보세요."
    ),
    NAVY(
            ImageTagCategory.COLOR,
            "네이비",
            List.of("남색", "딥블루"),
            "차분하지만 무게감 있는 어두운 블루 톤을 담습니다. 밤하늘, 딥블루 의상, 정제된 분위기의 이미지를 기록해보세요."
    ),
    SILVER(
            ImageTagCategory.COLOR,
            "실버",
            List.of("메탈", "은"),
            "차갑고 반사되는 금속성 색감을 의미합니다. 메탈 오브제, 인공적인 빛, 미래적인 인상의 사진을 올려보세요."
    ),
    MONO(
            ImageTagCategory.COLOR,
            "모노",
            List.of("단색", "무채"),
            "하나의 색감으로 통일된 시각적 인상을 담는 태그입니다. 단색 구성, 색의 반복, 미니멀한 장면을 추천해요."
    ),
    NEUTRAL(
            ImageTagCategory.COLOR,
            "뉴트럴",
            List.of("차분", "중성"),
            "특정 색이 두드러지지 않는 균형 잡힌 톤을 의미합니다. 차분한 팔레트, 자연스러운 조합의 이미지를 올려보세요."
    ),
    TONE(
            ImageTagCategory.COLOR,
            "톤",
            List.of("색감", "무드"),
            "색의 밝기와 농도 자체에 집중한 감각을 담습니다. 전체적인 색감의 흐름이 느껴지는 사진을 기록해보세요."
    ),
    CONTRAST(
            ImageTagCategory.COLOR,
            "대비",
            List.of("강약", "대비감"),
            "색과 명암의 차이가 분명하게 드러나는 상태를 의미합니다. 강한 밝기 차이, 선명한 경계가 있는 장면을 추천해요."
    ),
    PALE(
            ImageTagCategory.COLOR,
            "옅은",
            List.of("연한", "소프트", "페일"),
            "색이 희미하게 남아 있는 가벼운 톤을 담습니다. 연한 색감, 흐릿한 배경, 소프트한 이미지를 올려보세요."
    ),

    //======Texture
    MATTE(
            ImageTagCategory.TEXTURE,
            "매트",
            List.of("무광", "건조한"),
            "빛이 튀지 않고 표면이 차분하게 눌린 질감을 나타냅니다. 무광 제품, 건조한 소재, 반사 없는 오브제를 올려보세요."
    ),
    LEATHER(
            ImageTagCategory.TEXTURE,
            "가죽",
            List.of("레더", "질감"),
            "단단한 결, 주름, 광택 등 가죽 특유의 존재감을 담는 태그입니다. 가방·신발·재킷처럼 ‘사용감’이 드러나는 가죽 장면을 추천해요."
    ),
    LINEN(
            ImageTagCategory.TEXTURE,
            "린넨",
            List.of("내추럴", "패브릭"),
            "통기감 있고 결이 살아 있는 자연 섬유의 질감을 의미합니다. 구김이 매력인 패브릭, 여름빛, 내추럴한 착장 이미지를 올려보세요."
    ),
    COTTON(
            ImageTagCategory.TEXTURE,
            "면",
            List.of("코튼", "부드러움"),
            "가장 일상적인 ‘부드러운 기본 질감’을 담는 태그입니다. 티셔츠, 침구, 흰 셔츠처럼 편안한 면의 결을 기록해보세요."
    ),
    DENIM(
            ImageTagCategory.TEXTURE,
            "데님",
            List.of("청", "진"),
            "두께감 있는 직조 결, 워싱, 색 빠짐 같은 데님 무드를 담습니다. 청바지·자켓·가방 등 데님의 텍스처가 보이는 컷을 올려보세요."
    ),
    CHROME(
            ImageTagCategory.TEXTURE,
            "크롬",
            List.of("메탈", "반사"),
            "거울처럼 주변을 강하게 반사하는 매끈한 금속 질감을 뜻합니다. 반사로 풍경이 왜곡되는 오브제, 메탈릭한 하이라이트를 담아보세요."
    ),
    STEEL(
            ImageTagCategory.TEXTURE,
            "스틸",
            List.of("쇠", "금속"),
            "차갑고 단단한 금속 표면의 무게감을 나타냅니다. 공업적인 오브제, 직선 구조물, 메탈 프레임 같은 장면을 추천해요."
    ),

    CONCRETE(
            ImageTagCategory.TEXTURE,
            "콘크리트",
            List.of("시멘트", "거친"),
            "거칠고 미세한 입자가 느껴지는 무채색 표면을 의미합니다. 벽·바닥·기둥처럼 질감이 살아 있는 회색 풍경을 올려보세요."
    ),
    GLASS(
            ImageTagCategory.TEXTURE,
            "유리",
            List.of("투명", "반사"),
            "투명함, 반사, 굴절이 동시에 존재하는 재질을 담습니다. 창 너머 풍경, 유리컵, 반사된 빛이 겹치는 장면을 기록해보세요."
    ),
    PAPER(
            ImageTagCategory.TEXTURE,
            "종이",
            List.of("페이퍼", "질감"),
            "얇은 결, 인쇄감, 접힘 같은 ‘아날로그 표면’을 표현합니다. 노트, 포스터, 영수증, 잡지처럼 손에 잡히는 기록을 올려보세요."
    ),
    WOOD(
            ImageTagCategory.TEXTURE,
            "나무",
            List.of("우드", "자연"),
            "나뭇결과 톤의 온도가 주는 안정적인 질감을 뜻합니다. 우드 가구, 마루, 나무 표면의 결이 드러나는 사진을 추천해요."
    ),
    FABRIC(
            ImageTagCategory.TEXTURE,
            "패브릭",
            List.of("천", "직물"),
            "직물의 짜임, 두께, 레이어가 느껴지는 소재감을 담습니다. 커튼, 러그, 이불처럼 ‘결’이 보이는 천의 장면을 올려보세요."
    ),
    SMOOTH(
            ImageTagCategory.TEXTURE,
            "매끈",
            List.of("부드러움"),
            "요철 없이 정리된 표면과 ‘손에 닿을 듯한’ 촉감을 의미합니다. 매끈한 벽, 세라믹, 매끈한 피부나 오브제의 표면을 담아보세요."
    ),
    ROUGH(
            ImageTagCategory.TEXTURE,
            "거침",
            List.of("투박", "러프"),
            "표면이 깨끗하지 않고 까슬하거나 울퉁불퉁한 질감을 나타냅니다. 거친 돌, 낡은 벽, 텍스처가 강한 오브제를 올려보세요."
    ),
    WORN(
            ImageTagCategory.TEXTURE,
            "낡음",
            List.of("빈티지", "사용감"),
            "시간과 사용이 남긴 흔적, 마모감, 바랜 결을 담는 태그입니다. 빈티지 소품, 스크래치, 오래된 표면처럼 ‘시간이 보이는’ 장면을 기록해보세요."
    ),

    //======Space
    MIRROR(
            ImageTagCategory.SPACE,
            "거울",
            List.of("거울샷", "셀카"),
            "거울에 비친 장면을 담는 태그입니다. 그날의 착장, 손에 든 폰, 반사로 겹쳐진 구도를 올려보세요."
    ),
    DOWNTOWN(
            ImageTagCategory.SPACE,
            "도심",
            List.of("다운타운", "중심가", "거리"),
            "밀도 높은 도시의 리듬과 속도를 담는 태그입니다. 간판, 사람 흐름, 빌딩 사이의 빛처럼 도심 특유의 텐션을 기록해보세요."
    ),
    STAIR(
            ImageTagCategory.SPACE,
            "계단",
            List.of("나선형", "반복"),
            "올라가고 내려가는 ‘리듬’이 시각적으로 보이는 공간 요소입니다. 계단의 반복, 난간 라인, 발걸음 시점의 구도를 올려보세요."
    ),
    COUNTRY(
            ImageTagCategory.SPACE,
            "시골",
            List.of("컨츄리", "한적한", "자연"),
            "느슨한 시간감과 여백이 느껴지는 장소의 분위기를 담습니다. 낮은 건물, 들판, 조용한 골목처럼 ‘느림’이 보이는 풍경을 추천해요."
    ),
    WALL(
            ImageTagCategory.SPACE,
            "벽",
            List.of("배경"),
            "공간의 ‘무드’를 결정하는 가장 큰 면을 담는 태그입니다. 색감 있는 벽, 질감이 살아 있는 표면, 그림자가 앉은 벽을 올려보세요."
    ),
    TABLE(
            ImageTagCategory.SPACE,
            "테이블",
            List.of("책상"),
            "일상의 활동이 모이는 평면의 감각을 기록합니다. 커피잔, 책, 노트북 등 테이블 위 ‘배치’가 느껴지는 장면을 추천해요."
    ),
    CHAIR(
            ImageTagCategory.SPACE,
            "의자",
            List.of("좌석"),
            "누군가의 ‘머무름’이 상상되는 오브제를 담는 태그입니다. 혼자 놓인 의자, 디자인이 독특한 체어, 빛이 걸린 좌석을 올려보세요."
    ),
    FLOOR(
            ImageTagCategory.SPACE,
            "바닥",
            List.of("공간"),
            "시선 아래에 깔린 패턴과 질감으로 공간의 결을 보여줍니다. 타일, 마루, 러그처럼 발밑 디테일이 드러나는 컷을 기록해보세요."
    ),
    DOOR(
            ImageTagCategory.SPACE,
            "문",
            List.of("출입"),
            "안과 밖을 나누는 경계의 분위기를 담습니다. 문틈의 빛, 손잡이 디테일, 입구에서 느껴지는 ‘시작/끝’의 장면을 올려보세요."
    ),
    SHELF(
            ImageTagCategory.SPACE,
            "선반",
            List.of("진열"),
            "취향이 ‘배열’로 드러나는 공간을 의미합니다. 책장, 소품 진열, 향·화장품처럼 나만의 컬렉션이 보이는 선반을 담아보세요."
    ),
    ROOM(
            ImageTagCategory.SPACE,
            "방",
            List.of("실내"),
            "사적인 공간에서 드러나는 톤을 기록합니다. 빛, 물건, 배치처럼 지금 이 공간의 분위기가 느껴지는 장면을 보여주세요."
    ),
    CORNER(
            ImageTagCategory.SPACE,
            "모서리",
            List.of("구석"),
            "공간의 ‘끝’에서 생기는 조용한 구도를 담습니다. 벽과 바닥이 만나는 지점, 작은 배치, 비어 있는 코너를 추천해요."
    ),
    STREET(
            ImageTagCategory.SPACE,
            "거리",
            List.of("길"),
            "움직임이 쌓이는 ‘외부의 일상’을 담는 태그입니다. 횡단보도, 골목의 간판, 지나가는 사람의 실루엣 같은 장면을 올려보세요."
    ),
    CITY(
            ImageTagCategory.SPACE,
            "도시",
            List.of("어반"),
            "도시 전체의 톤과 구조감을 담습니다. 스카이라인, 건물의 패턴, 창문 불빛처럼 ‘도시의 형태’가 보이는 컷을 기록해보세요."
    ),
    CAFE(
            ImageTagCategory.SPACE,
            "카페",
            List.of("커피숍"),
            "머무름과 관찰이 동시에 가능한 공간의 무드를 담습니다. 테이블 위 구성, 메뉴판, 조명, 창가 자리를 담아보세요."
    ),

    //======Daily
    COFFEE(
            ImageTagCategory.DAILY,
            "커피",
            List.of("아메리카노", "에스프레소", "커피잔"),
            "하루의 리듬을 잡아주는 한잔을 담은 태그예요. 커피잔이 주인공이 아니어도 좋아요. 카페의 공기, 손의 움직임, 책상 위 구성처럼 ‘커피가 있는 순간’을 담아보세요."
    ),

    MATCHA(
            ImageTagCategory.DAILY,
            "말차",
            List.of("그린티", "녹차", "맛차"),
            "초록빛의 차분함, 부드러운 쌉싸름함이 떠오르는 태그예요. 말차 라떼뿐 아니라 그린 톤의 디저트, 패키지, 조명처럼 은근한 무드를 올려보세요."
    ),

    BAKERY(
            ImageTagCategory.DAILY,
            "베이커리",
            List.of("빵"),
            "바삭함, 결, 온기 같은 디테일이 살아나는 태그예요. 빵 자체보다도 포장지, 트레이, 빵집의 진열이나 조명처럼 갓 구운 분위기를 포착해보세요."
    ),

    WALK(
            ImageTagCategory.DAILY,
            "산책",
            List.of("걷기"),
            "목적지 없는 이동에서 생기는 여백을 담는 태그예요. 하늘, 발끝, 그림자, 길의 질감처럼 걷는 중에 눈에 들어온 것을 가볍게 올려보세요."
            ),

    REST(
            ImageTagCategory.DAILY,
            "휴식",
            List.of("쉼", "여유"),
            "나만의 쉼을 담는 태그예요. 누워 있는 이불 결, 비워둔 화면, 조용한 테이블처럼 쉬는 시간을 만든 요소를 기록해보세요."
    ),

    PAUSE(
            ImageTagCategory.DAILY,
    "멈춤",
            List.of("브레이크", "퓨즈", "여유"),
    "잠깐 멈춘 타이밍에서 생기는 여운을 담아요. 하던 일을 내려둔 손, 끊긴 음악, 중간 저장된 화면 같은 장면을 올려보세요."
    ),

    WEEKEND(
            ImageTagCategory.DAILY,
            "주말",
            List.of("휴일"),
            "평일과 다른 속도, 다른 선택이 보이는 태그예요. 늦잠의 빛, 느슨한 외출 준비, 한 번 더 머문 자리처럼 ‘주말의 템포’를 담아보세요."
    ),

    DAILY(
            ImageTagCategory.DAILY,
            "일상",
            List.of("데일리"),
            "특별하지 않아서 더 선명해지는 태그예요. 반복되는 구도나 물건, 매일 보는 풍경처럼 나를 설명하는 습관적인 장면을 올려도 좋아요."
    ),

    ROUTINE(
            ImageTagCategory.DAILY,
            "루틴",
            List.of("반복", "일상", "습관"),
            "반복이 쌓이면서 만들어지는 나를 담아요. 아침 준비, 책상 세팅, 운동 전 준비물처럼 루틴을 구성하는 조각을 하나씩 기록해보세요."
    ),

    ALONE(
            ImageTagCategory.DAILY,
            "혼자",
            List.of("솔로", "홀로", "하나"),
            "나에게 집중하는 상태를 담는 태그예요. 혼자 앉은 자리, 이어폰 낀 순간, 혼자만의 동선처럼 혼자의 공기를 올려보세요."
    ),

    // ======Fashion
    OOTD(
            ImageTagCategory.FASHION,
            "오오티디",
            List.of("데일리룩", "코디", "패션"),
            "오늘의 스타일을 기록하는 태그예요. 전신샷이 아니어도 좋아요. 소매, 신발, 가방, 거울 반사처럼 디테일로도 충분해요."
    ),

    OUTFIT(
            ImageTagCategory.FASHION,
            "착장",
            List.of("스타일", "아웃핏", "패션"),
            "룩의 조합이 보이는 순간을 담는 태그예요. 옷걸이에 걸린 세트, 의자 위에 놓인 레이어처럼 코디가 완성되는 장면도 잘 어울려요."
    ),

    LAYERING(
            ImageTagCategory.FASHION,
            "레이어드",
            List.of("겹쳐입는", "코디", "패션"),
            "겹쳐 입을 때 생기는 선, 두께, 대비를 담는 태그예요. 니트+셔츠의 칼라, 코트 안쪽, 목도리 결처럼 ‘겹’이 보이는 디테일을 올려보세요."
    ),

    SHOES(
            ImageTagCategory.FASHION,
            "신발",
            List.of("슈즈", "패션", "코디"),
            "새 신발도 좋고 낡은 신발도 좋아요. 발끝 시점, 신발장, 바닥 질감과 함께 찍어도 멋있어요."
    ),

    COAT(
            ImageTagCategory.FASHION,
            "코트",
            List.of("아우터"),
            "실루엣이 단정해지는 계절의 무드를 담는 태그예요. 입은 모습뿐 아니라 걸어둔 코트의 형태, 버튼이나 카라 디테일처럼 ‘무게감’이 보이는 장면도 좋아요."
    ),

    KNIT(
            ImageTagCategory.FASHION,
            "니트",
            List.of("스웨터", "포근한", "겨울"),
            "촉감이 먼저 떠오르는 포근한 분위기의 태그예요. 조직감이 보이는 클로즈업, 소매 끝, 의자에 걸친 니트처럼 결이 살아 있는 장면을 담아보세요."
    ),

    TOTE(
            ImageTagCategory.FASHION,
            "토트백",
            List.of("가방"),
            "‘들고 다니는 취향’이 드러나는 태그예요. 가방 자체보다도 안에 든 것, 손에 쥔 방식, 바닥에 놓인 순간처럼 사용감이 보이면 더 좋아요."
    ),

    DENIMFIT(
            ImageTagCategory.FASHION,
            "데님룩",
            List.of("청청", "코디", "패션"),
            "워싱과 결이 만들어내는 빈티지한 텐션의 태그예요. 청청 코디뿐 아니라 데님이 섞인 한 포인트, 스티치나 주름처럼 데님 특유의 표정을 올려보세요."
    ),

    BLACKWEAR(
            ImageTagCategory.FASHION,
            "블랙웨어",
            List.of("올블랙", "검정"),
            "올블랙이 아니어도 좋아요. 검정이 만든 실루엣, 소재 대비를 보여줘도 멋있어요."
    ),

    MINIMAL(
            ImageTagCategory.FASHION,
            "미니멀",
            List.of("심플", "깔끔한"),
            "색을 줄인 코디, 군더더기 없는 실루엣, 정돈된 옷장이나 행거처럼 절제된 장면도 잘 어울려요."
    ),

    //======Media

    FILM(
            ImageTagCategory.MEDIA,
            "필름",
            List.of("필름카메라"),
            "필름 특유의 온도와 결이 있는 태그예요. 빛 샘, 색 번짐, 흔들림처럼 필름 같은 흔적이 보이면 잘 어울려요."
    ),

    ANALOG(
            ImageTagCategory.MEDIA,
            "아날로그",
            List.of("빈티지", "옛날"),
            "디지털의 선명함보다 손맛과 물성을 더 믿고 싶은 날의 태그예요. 종이, 잉크, 다이얼, 오래된 기기처럼 ‘손으로 다뤄지는 감각’이 느껴지는 장면을 올려보세요."
    ),

    VINYL(
            ImageTagCategory.MEDIA,
            "바이닐",
            List.of("레코드", "LP"),
            "LP 자체뿐 아니라 커버 아트, 턴테이블의 회전, 바늘 내려가는 순간처럼 사운드의 분위기를 담아도 좋아요."
    ),

    POSTER(
            ImageTagCategory.MEDIA,
            "포스터",
            List.of("그래픽"),
            "한 장의 이미지로 공간 분위기가 바뀌는 순간을 담는 태그예요. 전시장, 길거리, 방 한 켠의 포스터를 올려보세요."
    ),

    TYPOGRAPHY(
            ImageTagCategory.MEDIA,
            "타이포",
            List.of("글자", "텍스트", "글"),
            "글자가 아름다워 보이는 순간을 담는 태그예요. 간판, 패키지, 책 표지, 티켓처럼 글자의 형태나 배치가 예쁜 장면을 줍듯 기록해보세요."
    ),

    BOOK(
            ImageTagCategory.MEDIA,
            "책",
            List.of("독서", "글", "작가"),
            "펼친 페이지, 책갈피, 카페 테이블 위 책처럼 지금의 관심사가 스쳐 지나가는 장면을 올려보세요."
    ),

    NOTE(
            ImageTagCategory.MEDIA,
            "노트",
            List.of("메모", "글", "손글씨"),
            "생각이 정리되기 전의 흔적을 담는 태그예요. 손글씨, 낙서, 체크표시, 구겨진 메모처럼 완벽하지 않아 더 솔직한 기록을 올려도 좋아요."
    ),

    IPHONE(
            ImageTagCategory.MEDIA,
            "아이폰",
            List.of("스마트폰"),
            "손에 들린 스크린이 내 시선이 되는 태그예요. 거울샷의 폰, 잠금화면, 카메라 화면 반사처럼 아이폰이 장면 안에 자연스럽게 섞인 컷을 올려보세요."
    ),

    HEADPHONE(
            ImageTagCategory.MEDIA,
            "헤드폰",
            List.of("음악", "헤드셋"),
            "헤드폰 착용샷뿐 아니라 가방에 걸린 헤드폰, 책상 위 케이블, 음악 듣는 동선처럼 소리의 분위기를 담아보세요."
    ),

    ARCHIVE(
            ImageTagCategory.MEDIA,
            "아카이브",
            List.of("기록", "저장"),
            "쌓이는 기록 자체가 취향이 되는 태그예요. 사진 모음, 폴더, 스크랩북, 수집한 티켓처럼 ‘모아둔 것’의 질서와 반복을 보여줘도 잘 어울려요."
    ),

    //======Travel
    MOUNTAIN(
            ImageTagCategory.TRAVEL,
            "산",
            List.of("산", "등산", "정상"),
            "공기가 달라지는 고도감의 태그예요. 능선의 라인, 바위의 결, 위로 열리는 시야처럼 ‘올라온 느낌’이 전해지는 장면을 담아보세요."
    ),

    OCEAN(
            ImageTagCategory.TRAVEL,
            "바다",
            List.of("바다", "파도", "해변", "여행"),
            "끝없이 이어지는 수평과 깊은 색의 태그예요. 파도보다도 면으로 느껴지는 바다, 바람의 기운이 담긴 장면도 잘 어울려요."
    ),

    BEACH(
            ImageTagCategory.TRAVEL,
            "해변",
            List.of("해변", "모래사장", "파도"),
            "모래와 물이 만나는 경계의 태그예요. 발자국, 젖은 모래, 해변 위 소지품 배치처럼 여행의 흔적이 보이는 컷을 올려보세요."
    ),

    FOREST(
            ImageTagCategory.TRAVEL,
            "숲",
            List.of("숲", "나무", "초록", "그린"),
            "빽빽한 초록과 그늘의 공기를 담아요. 나뭇잎 사이로 스며드는 빛, 흙길의 질감 같은 장면이 잘 어울려요."
    ),

    LAKE(
            ImageTagCategory.TRAVEL,
            "호수",
            List.of("호수", "물가", "파도"),
            "움직임이 최소화된 물의 풍경을 담는 태그예요. 반사된 하늘, 잔잔한 수면처럼 고요함이 보이는 장면을 기록해보세요."
    ),

    ISLAND(
            ImageTagCategory.TRAVEL,
            "섬",
            List.of("섬", "바다", "여행"),
            "일상에서 떨어져 나온 느낌의 태그예요. 선착장, 섬길, 제한된 동선처럼 ‘고립감과 해방감’이 함께 느껴지는 장면을 담아보세요."
    ),

    ROADTRIP(
            ImageTagCategory.TRAVEL,
            "로드트립",
            List.of("드라이브", "자동차", "길거리"),
            "목적지보다 이동의 리듬이 중요한 태그예요. 차창 밖 풍경, 휴게소, 길 위의 순간처럼 흐름이 느껴지는 컷을 올려보세요."
    ),

    VIEWPOINT(
            ImageTagCategory.TRAVEL,
            "전망대",
            List.of("전망", "뷰", "여행"),
            "한 번에 펼쳐지는 시야를 담아요. 난간, 창가, 아래를 내려다보는 구도처럼 ‘열린 시선’이 느껴지는 장면을 추천해요."
    ),

    STREETFOOD(
            ImageTagCategory.TRAVEL,
            "길거리음식",
            List.of("길거리음식", "포장마차", "간식"),
            "여행지에서만 먹는 즉흥의 맛을 담는 태그예요. 음식보다도 포장, 손의 위치, 주변 공기처럼 현장의 분위기를 함께 올려보세요."
    ),

    LOCALSPOT(
            ImageTagCategory.TRAVEL,
            "로컬스팟",
            List.of("로컬", "동네맛집", "현지", "여행"),
            "유명하지 않아 더 기억에 남는 장소의 태그예요. 간판, 메뉴판, 골목의 결처럼 지역성이 느껴지는 장면을 담아보세요."
    ),

    HOTEL(
            ImageTagCategory.TRAVEL,
            "호텔",
            List.of("호텔", "숙소", "수영장"),
            "하루를 머무는 공간의 톤을 담아요. 로비의 조명, 복도, 침대 위 빛처럼 ‘머무는 감각’이 느껴지는 장면이 잘 어울려요."
    ),

    RESORT(
            ImageTagCategory.TRAVEL,
            "리조트",
            List.of("휴양지", "수영장", "리조트"),
            "느슨한 시간감과 여유가 드러나는 태그예요. 수영장 가장자리, 라운지 체어, 열린 풍경처럼 쉬는 리듬을 담아보세요."
    ),

    AIRPLANE(
            ImageTagCategory.TRAVEL,
            "비행기",
            List.of("하늘", "공항"),
            "이동 자체가 설레는 태그예요. 창밖 구름, 티켓처럼 ‘여행이 시작되는 느낌’을 담아보세요."
    ),

    FERRY(
            ImageTagCategory.TRAVEL,
            "페리",
            List.of("배", "선착장", "항구"),
            "물 위를 이동하는 특유의 템포를 담아요. 갑판, 손잡이, 바다를 가르는 시선처럼 이동의 감각이 보이는 컷을 올려보세요."
    ),

    SOUVENIR(
            ImageTagCategory.TRAVEL,
            "기념품",
            List.of("기념품", "굿즈", "선물"),
            "여행의 감각을 물건으로 남긴 태그예요. 엽서, 로컬 굿즈, 포장된 형태처럼 ‘가져온 취향’을 담아도 좋아요."
    );

    private final ImageTagCategory imageTagCategory;
    private final String tagKo;
    private final List<String> synonyms;
    private final String description;
}
