INSERT INTO personality
(created_at, updated_at, bad_personality_image_url, bad_personality_name, color, description,
 good_personality_image_url, good_personality_name, image_url, name, recommend_title, recommend_todo, title)
VALUES ("2022-01-01", "2022-01-01", "bad_personality_image_url", "bad_personality_name", "GRAY", "description",
        "good_personality_image_url", "good_personality_name", "image_url", "name", "recommend_title", "recommend_todo",
        "title"),
       ("2022-01-01", "2022-01-01", "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/green.png",
        "룰 세터 육각이", "YELLOW",
        "민감하게 생각하는 생활 영역이 거의 없어\n동글동글하게 공동생활에 쉽게 적응할 수 있어요.\n동글이님과 함께 생활하는 룸메이트를 위한 Tip!\n배려가 필요한 영역이 있다면 동글이 호미와\n직접 얘기해 보는 건 어떤가요?",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/red.png", "슈퍼 팔로워 셋돌이",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/yellow.png", "늘 행복한 동글이", "동글이가 만들면 좋은 Rules",
        "모두가 함께하는 대청소의 날 정하기!\n외출 시 가스벨브, 전등 확인하기!", "어떤 상황에서도 Happy~"),
       ("2022-01-01", "2022-01-01", "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/green.png",
        "룰 세터 육각이", "RED",
        "크게 민감한 생활 영역 없이 유연한 셋돌이!\n독립적이고 적응력이 높아 공동생활에 딱 적합해요.\n셋돌이님과 함께 생활하는 룸메이트를 위한 Tip!\n셋돌이 호미가 바빠서 까먹고 있던 우리 집\nRules나 to do를 알려주는 건 어떤가요?",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/blue.png", "룸메 맞춤형 네각이",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/red.png", "슈퍼 팔로워 셋돌이", "셋돌이가 만들면 좋은 Rules",
        "미사용 전자제품 코드는 꼭 뽑아두기!\n외출 시 문 잠금 확인하기!", "오히려 좋아~ 가보자고!"),
       ("2022-01-01", "2022-01-01", "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/yellow.png",
        "늘 행복한 동글이", "BLUE",
        "높지도 낮지도 않은 딱 좋은\n평균적인 민감도를 가지고 있어요.\n네각이님과 함께 생활하는 룸메이트를 위한 Tip!\n네각이의 생활 성향 그래프를 보고 특정한\n영역에 민감도가 쏠려있다면 살짝 배려해 보아요~",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/purple.png", "하이레벨 오돌이",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/blue.png", "룸메 맞춤형 네각이", "네각이가 만들면 좋은 Rules",
        "밥 먹고 설거지는 바로 하기!\n소등 시간 이후에는 조용히 하기!", "모두모두 내게 모여라!"),
       ("2022-01-01", "2022-01-01", "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/yellow.png",
        "늘 행복한 동글이", "PURPLE",
        "여러 생활 영역에 대해 존중의 기준이 대체로 높아\n다른 룸메이트의 민감한 부분과 무심한 부분\n모두를 충분히 공감하고 이해할 수 있어요.\n오돌이님과 함께 생활하는 룸메이트를 위한 Tip!\n말하기 어려운 게 있는지 슬쩍 물어보면 아주 센스쟁이!",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/green.png", "룰 세터 육각이",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/purple.png", "하이레벨 오돌이", "오돌이가 만들면 좋은 Rules",
        "쓰레기 배출 요일 확인하고 정리하기!\n방 안에서 음식물 섭취하지 않기!", "오케바리~ 알겠다!"),
       ("2022-01-01", "2022-01-01", "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/red.png",
        "슈퍼 팔로워 삼각이", "GREEN",
        "주변에 관심이 많고 전반적인 생활 영역에서\n높은 수준의 섬세함을 지녀 다른 룸메이트들의\n생활도 세심하게 배려해줄 수 있어요.\n육각이님과 함께 생활하는 룸메이트를 위한 Tip!\n우리 집 Rules 추천을 받아보는 건 어떤가요?",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/purple.png", "하이레벨 오돌이",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality/green.png", "룰 세터 육각이", "육각이가 만들면 좋은 Rules",
        "빨래가 끝나면 바로 옷 꺼내기!\n샤워 후 머리카락 치우기!", "나는 공동생활 백과사전!");

INSERT INTO personality_test
(created_at, updated_at, answers, idx, image_url, question, question_type)
VALUES ("2022-01-01", "2022-01-01", "스탠드를 키고 자 방이 매우 밝다.\n햇빛이 비춰 자연스럽게 방을 밝힌다.\n암막 커튼이 쳐져있어, 완전히 어둡다.", 1,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-01.png", "알람 소리에 눈을 뜬 당신\n방 안은 어떤가요?",
        "LIGHT"),
       ("2022-01-01", "2022-01-01",
        "어? 샴푸를 다 썼네? 미래의 내가 치우겠지~\t하며 일단 아무데나 둔다.\n지금은 바쁘니 외출 후 치워야지!\t욕실 문 근처에 둔다.\n미리미리 치워둬야 고생을 안 해.\t샴푸통을 분리해 재활용으로 버린다.",
        2, "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-02.png",
        "외출 준비를 하려는데\n샴푸통이 비어있는 것 같다.", "CLEAN"),
       ("2022-01-01", "2022-01-01",
        "입었던 옷, 빨래한 옷들이 뒤죽박죽 섞여있다.\n입었던 옷은 대충 놓여 있지만,\t빨래한 옷들은 개어져 있다.\n깔끔하게 정리된 옷장!\t모든 옷이 잘 정리되어 개어있다.", 3,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-03.png", "옷을 입으려 옷장을 열었다.\n옷장의 상태는?",
        "CLEAN"),
       ("2022-01-01", "2022-01-01", "냄새? 음, 잘 모르겠는데?\n조금 나는 것 같긴 한데,\t그래도 이 정도면 괜찮아~\n어우, 잘못 세탁했다. 다시 빨아야겠는데?", 4,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-04.png",
        "집을 나서는데 룸메이트가 덜 마른 옷을\n내밀며 냄새가 나는지 묻는다면?", "SMELL"),
       ("2022-01-01", "2022-01-01", "어, 뭐야? 공사하네! 언제 시작했지?\n아, 간간히 들리던 소리가 공사소리였구나!\n어쩐지 많이 시끄럽더라, 공사야 빨리 끝나라!", 5,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-05.png", "집 밖으로 나오니\n집 앞 공사 환경이 보인다.",
        "NOISE"),
       ("2022-01-01", "2022-01-01", "음, 향수 냄세네! 하고 갈 길을 간다.\n아, 냄새... 향수 뿌렸나 보네?\n향수 냄새가 심하네... 라고 생각하며\t빨리 걷는다.", 6,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-06.png",
        "약속장소를 가는 길,\n진한 향수를 뿌린 사람이 지나간다.", "SMELL"),
       ("2022-01-01", "2022-01-01", "내가 분위기 메이커!\t바로 새로운 이야기를 꺼낸다.\n가끔씩 궁금한 걸 물으며 말을 해보려 한다.\n오히려 편하다, 정적을 즐긴다.", 7,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-07.png",
        "친구와 식당에 도착해\n주문을 하고 나니 순간 정적이 찾아왔다.", "INTROVERSION"),
       ("2022-01-01", "2022-01-01",
        "생활하다보면 그 정도 소음은 나지.\t 괜찮다고 한다.\n조금 신경쓰이긴 해~ 라고 말하며\t스몰토크를 이어나간다.\n여전히 시끄러워서 어떻게 말을 해야\t할지 고민 중이라고 이야기한다.", 8,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-08.png",
        "친구가 옆 방에서 들린다고 했던\n음악소리는 괜찮은지 물어본다.", "NOISE"),
       ("2022-01-01", "2022-01-01",
        "반갑게 인사하고 여러가지 안부를 묻는다.\n다가가 간단히 인사를 하며\tK-약속만 잡고 헤어진다.\n반갑긴 하지만 부담스러워..\t나를 못 알아봤길 바라면서 조용히 지나친다.", 9,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-09.png",
        "식사를 마치고 식당을 나서는데\n저 멀리 왠지 낯익은 얼굴이 보인다.", "INTROVERSION"),
       ("2022-01-01", "2022-01-01",
        "와, 맛있겠다! 음식 냄새가 좋다고 생각한다.\n음식 냄새가 조금 신경 쓰이지만 내 방까지\t풍기는 것은 아니니 괜찮다고 생각한다.\n조용히 창문을 열어 환기시킨다.", 10,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-10.png",
        "다시 돌아온 집, 룸메이트가\n배달 음식을 거실에서 먹고 있다.", "SMELL"),
       ("2022-01-01", "2022-01-01",
        "침대에는 옷들, 책상 위는 보던 책.\t마구 섞여 있지만 나만의 규칙은 있다.\n엄청 깔끔하진 않지만 어느 정도 정리되어 있다.\n깔끔하게  정돈된 공간이 안정감을 준다.", 11,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-11.png",
        "친구를 뒤로 하고 내 방에 도착했다.\n내 방의 상태는?", "CLEAN"),
       ("2022-01-01", "2022-01-01",
        "쉬는 날 친구 만나고 놀아야지~\t바로 전화해 약속을 잡는다.\n음, 일단 약속을 잡지는 말고\t연락오면 나가자.\n집에서 충전할 시간이 필요해\t침대랑 합체!", 12,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-12.png",
        "책상에 앉아 일정을 정리하는데\n내일은 쉬는 날, 친구와 약속을 잡을까?", "INTROVERSION"),
       ("2022-01-01", "2022-01-01",
        "문이 살짝 열려 있어 복도의 불빛이 보이지만\t특별히 불편함을 느끼지 않고 바로 잠든다.\n불을 껐지만 모니터 깜빡임이 조금\t신경쓰여 뒤척이다 잠에 든다.\n잠을 잘 때는 어두운 게 좋아\t안대를 끼고 잔다.",
        13, "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-13.png", "하루를 마무리하고 잘 준비를 마쳤다.",
        "LIGHT"),
       ("2022-01-01", "2022-01-01",
        "아침에 확인해야지,\t별 생각 없이 무시하고 잔다.\n진동이 조금 신경쓰이지만 계속\t울리지는 않겠지 생각하며 잠을 청한다.\n바로 전화를 무음모드로 돌려놓는다.", 14,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-14.png", "잠들기 직전 진동으로 해놓은\n전화가 울린다.",
        "NOISE"),
       ("2022-01-01", "2022-01-01",
        "스탠드를 켠지도 모르고 잘 잔다.\n조금 뒤척이다 스탠드 쪽을\t등지고 잠을 청한다.\n침대에서 일어나 친구에게 스탠드\t불을 줄여달라고 이야기한다.", 15,
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/personality-test/test-15.png",
        "모두가 자는 새벽,\n갑자기 친구가 스탠드를 밝게 켰다.", "LIGHT");

INSERT INTO badge
    (created_at, updated_at, info, image_url)
VALUES ("2022-01-01", "2022-01-01", "POUNDING_HOUSE",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/pounding-house.png"),
       ("2022-01-01", "2022-01-01", "I_AM_SUCH_A_PERSON",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/i-am-such-a-person.png"),
       ("2022-01-01", "2022-01-01", "OUR_HOUSE_HOMIES",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/our-house-homies.png"),
       ("2022-01-01", "2022-01-01", "I_DONT_EVEN_KNOW_ME",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/i-dont-even-know-me.png"),
       ("2022-01-01", "2022-01-01", "HOMIE_IS_BORN",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/homie-is-born.png"),
       ("2022-01-01", "2022-01-01", "TODO_ONE_STEP",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/todo-one-step.png"),
       ("2022-01-01", "2022-01-01", "GOOD_JOB", "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/good-job.png"),
       ("2022-01-01", "2022-01-01", "SINCERITY_KING_HOMIE",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/sincerity-king-homie.png"),
       ("2022-01-01", "2022-01-01", "TODO_MASTER",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/todo-master.png"),
       ("2022-01-01", "2022-01-01", "LETS_BUILD_A_POLE",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/lets-build-a-pole.png"),
       ("2022-01-01", "2022-01-01", "OUR_HOUSE_PILLAR_HOMIE",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/our-house-pillar-homie.png"),
       ("2022-01-01", "2022-01-01", "FEEDBACK_ONE_STEP",
        "https://team-hous.s3.ap-northeast-2.amazonaws.com/badge/feedback-one-step.png");

INSERT INTO deploy
    (id, created_at, updated_at, os, version, market_url)
VALUES (1, "2022-12-01", "2022-12-01", "iOS", "1.0.0", "https://apps.apple.com/kr/app/hous-/id1659976144"),
       (2, "2022-12-01", "2022-12-01", "AOS", "1.0.1",
        "https://play.google.com/store/apps/details?id=hous.release.androidhttps://play.google.com/store/apps/details?id=hous.release.android");
