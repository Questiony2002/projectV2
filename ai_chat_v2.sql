/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : ai_chat_v2

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 12/05/2025 11:13:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mbti_questions
-- ----------------------------
DROP TABLE IF EXISTS `mbti_questions`;
CREATE TABLE `mbti_questions`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `option_a` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `option_b` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dimension` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mbti_questions
-- ----------------------------
INSERT INTO `mbti_questions` VALUES (1, '在社交场合中，你通常会', '主动与他人交谈', '等待他人来找你聊天', 'EI');
INSERT INTO `mbti_questions` VALUES (2, '当面对新的任务时，你倾向于', '按照既定的方法执行', '尝试新的解决方案', 'SN');
INSERT INTO `mbti_questions` VALUES (3, '在做决定时，你更看重', '客观的事实和逻辑', '个人的感受和价值观', 'TF');
INSERT INTO `mbti_questions` VALUES (4, '你更喜欢', '提前计划和安排', '随机应变和灵活处理', 'JP');
INSERT INTO `mbti_questions` VALUES (5, '在团队工作中，你更倾向于', '积极发表意见', '倾听他人观点', 'EI');
INSERT INTO `mbti_questions` VALUES (6, '解决问题时，你更依赖', '实际经验和具体事实', '直觉和可能性', 'SN');
INSERT INTO `mbti_questions` VALUES (7, '评价他人时，你更注重', '公平和客观标准', '同理心和个人情况', 'TF');
INSERT INTO `mbti_questions` VALUES (8, '面对工作任务，你习惯', '制定详细的计划', '保持灵活和适应性', 'JP');
INSERT INTO `mbti_questions` VALUES (9, '休闲时间，你更喜欢', '参加社交活动', '独处或与密友相处', 'EI');
INSERT INTO `mbti_questions` VALUES (10, '学习新知识时，你偏好', '循序渐进的学习方法', '跳跃式的思维方式', 'SN');
INSERT INTO `mbti_questions` VALUES (11, '处理冲突时，你倾向于', '分析问题寻找解决方案', '考虑各方感受和需求', 'TF');
INSERT INTO `mbti_questions` VALUES (12, '日常生活中，你更重视', '秩序和规划', '自由和即兴', 'JP');
INSERT INTO `mbti_questions` VALUES (13, '与人交往时，你通常', '容易表达自己的想法', '需要时间组织语言', 'EI');
INSERT INTO `mbti_questions` VALUES (14, '面对新环境，你会', '关注具体细节', '注意整体印象', 'SN');
INSERT INTO `mbti_questions` VALUES (15, '做选择时，你倾向于', '依据理性分析', '跟随内心感受', 'TF');
INSERT INTO `mbti_questions` VALUES (16, '处理事务时，你习惯', '按部就班完成', '灵活调整优先级', 'JP');
INSERT INTO `mbti_questions` VALUES (17, '在会议中，你更倾向于', '积极参与讨论', '仔细聆听和思考', 'EI');
INSERT INTO `mbti_questions` VALUES (18, '解决问题时，你更看重', '实用性和可行性', '创新性和可能性', 'SN');
INSERT INTO `mbti_questions` VALUES (19, '面对批评，你会', '客观分析问题所在', '关注情感影响', 'TF');
INSERT INTO `mbti_questions` VALUES (20, '生活方式上，你偏好', '有规律的生活节奏', '随性自由的生活方式', 'JP');

-- ----------------------------
-- Table structure for mbti_types
-- ----------------------------
DROP TABLE IF EXISTS `mbti_types`;
CREATE TABLE `mbti_types`  (
  `type_code` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `characteristics` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `strengths` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `weaknesses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`type_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mbti_types
-- ----------------------------
INSERT INTO `mbti_types` VALUES ('ENFJ', '主人公', '富有魅力和鼓舞人心的领导者，能够打动他人的心灵。', '富有感染力的引导者，关注他人成长和团队和谐。', '领导能力强，善于沟通，富有同理心，重视关系。', '可能过于关注他人需求，容易忽视自我需求，过度理想化。');
INSERT INTO `mbti_types` VALUES ('ENFP', '竞选者', '热情洋溢、富有创造力的活力派，总能找到让生活变得更美好的理由。', '充满热情的自由思想家，善于发现可能性和激发他人潜能。', '创造力强，适应能力好，善于沟通，富有同情心。', '可能注意力分散，不够专注，难以坚持完成任务。');
INSERT INTO `mbti_types` VALUES ('ENTJ', '指挥官', '大胆、富有想象力的强势领导者，总能找到方法实现目标。', '天生的领导者，具有魄力和决断力，追求效率和成功。', '组织能力强，善于决策，目标导向，富有远见。', '可能显得专制，不够体贴，过于追求完美。');
INSERT INTO `mbti_types` VALUES ('ENTP', '辩论家', '聪明好奇的思想家，不会放过任何智力挑战的机会。', '善于辩论的创新者，喜欢挑战传统观念和探索可能性。', '思维敏捷，创意丰富，适应能力强，善于解决问题。', '可能不够专注，不善于执行，容易忽视细节。');
INSERT INTO `mbti_types` VALUES ('ESFJ', '执政官', '极有同情心的监护人，总是热心帮助他人。', '热心的照顾者，重视和谐与合作，关注他人福祉。', '善于照顾他人，组织能力强，重视传统，富有同理心。', '可能过于在意他人看法，难以处理冲突，容易情绪化。');
INSERT INTO `mbti_types` VALUES ('ESFP', '表演者', '自发性的、精力充沛的娱乐者，生活永远不会变得无聊。', '热情的表演者，善于享受生活，追求快乐和自由。', '乐观开朗，适应力强，善于交际，富有同理心。', '可能过于追求享乐，不够深入，难以做出承诺。');
INSERT INTO `mbti_types` VALUES ('ESTJ', '总经理', '出色的管理者，在管理事务和人员方面无可比拟。', '高效的组织者，重视秩序和规则，追求目标实现。', '执行力强，善于管理，务实高效，重视责任。', '可能过于固执，不够灵活，难以接受不同意见。');
INSERT INTO `mbti_types` VALUES ('ESTP', '企业家', '聪明、精力充沛的表演者，总是喜欢活在聚光灯下。', '灵活的行动者，善于把握机会，追求刺激和冒险。', '行动力强，适应能力好，善于解决问题，富有魅力。', '可能过于冲动，不够深思熟虑，难以长期规划。');
INSERT INTO `mbti_types` VALUES ('INFJ', '提倡者', '安静而神秘，同时富有启发性和理想主义。', '富有理想的引导者，关注他人成长和社会进步。', '洞察力强，富有同理心，有远见，重视和谐。', '可能过于理想化，容易完美主义，难以接受批评。');
INSERT INTO `mbti_types` VALUES ('INFP', '调停者', '富有诗意的理想主义者，总是愿意为了帮助他人而挺身而出。', '富有同情心的理想主义者，追求内心和谐和个人价值。', '创造力强，富有同理心，重视真诚，追求理想。', '可能过于敏感，不够现实，难以做出决定。');
INSERT INTO `mbti_types` VALUES ('INTJ', '建筑师', '具有想象力和战略性的思考者，一切都在计划中。', '富有想象力的战略家，具有创新思维，追求持续改进和完美。', '分析能力强，独立思考，善于规划，追求卓越。', '可能显得过于完美主义，不够灵活，社交能力较弱。');
INSERT INTO `mbti_types` VALUES ('INTP', '逻辑学家', '具有创新精神的发明家，对知识有着永不满足的渴望。', '富有创造力的思考者，喜欢探索理论和抽象概念。', '逻辑思维能力强，创新能力出众，独立思考。', '可能过于理论化，不够实际，容易忽视他人感受。');
INSERT INTO `mbti_types` VALUES ('ISFJ', '守卫者', '非常专注和温暖的守护者，总是准备保护关心的人。', '细心的照顾者，重视传统和稳定，关注他人需求。', '责任心强，有耐心，重视关系，注重细节。', '可能过于自我牺牲，不善于表达需求，难以说不。');
INSERT INTO `mbti_types` VALUES ('ISFP', '探险家', '灵活而富有魅力的艺术家，总是准备探索和体验新事物。', '富有艺术感的探索者，重视个人价值和审美体验。', '创造力强，感知力敏锐，重视和谐，富有同情心。', '可能过于随性，不够果断，难以规划长远。');
INSERT INTO `mbti_types` VALUES ('ISTJ', '物流师', '实际而注重事实的个人，可靠性毋庸置疑。', '负责任的组织者，重视传统和秩序，追求稳定和可靠。', '组织能力强，注重细节，可靠踏实，尽职尽责。', '可能过于保守，不够灵活，难以接受变化。');
INSERT INTO `mbti_types` VALUES ('ISTP', '鉴赏家', '大胆而实际的实验家，擅长使用任何形式的工具。', '灵活的问题解决者，善于处理危机，追求效率和实用。', '动手能力强，适应力好，善于解决问题，注重实效。', '可能缺乏耐心，不够感性，难以长期专注。');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `publish_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 951 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (941, '2025-05-12 03:12:02.840000', '2025-05-12', '心理咨询师职业资格认证取消后，谁在收割焦虑？', 'http://psy.china.com.cn/2025-05/12/content_43107107.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/12/content_43107107.htm');
INSERT INTO `news` VALUES (942, '2025-05-12 03:12:02.850000', '2025-05-12', '评论丨线上心理咨询乱象层出不穷，亟需被正视和解决', 'http://psy.china.com.cn/2025-05/12/content_43107185.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/12/content_43107185.htm');
INSERT INTO `news` VALUES (943, '2025-05-12 03:12:02.852000', '2025-05-12', '心理百科|关于抑郁症的躯体化症状 这些需要了解', 'http://psy.china.com.cn/2025-05/12/content_43107089.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/12/content_43107089.htm');
INSERT INTO `news` VALUES (944, '2025-05-12 03:12:02.854000', '2025-05-12', '专家学者云集山东：溯古寻理 为当代心理疏导解锁文化密钥', 'http://psy.china.com.cn/2025-05/12/content_43107049.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/12/content_43107049.htm');
INSERT INTO `news` VALUES (945, '2025-05-12 03:12:02.855000', '2025-05-12', '24小时不打烊！河北省“12356”心理援助热线正式开通', 'http://psy.china.com.cn/2025-05/12/content_43107011.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/12/content_43107011.htm');
INSERT INTO `news` VALUES (946, '2025-05-12 03:12:02.856000', '2025-05-10', '心理中国大讲堂5月10日回放:意义失落的年代,陪孩子一起\"过关\"', 'http://psy.china.com.cn/2025-05/10/content_43106834.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/10/content_43106834.htm');
INSERT INTO `news` VALUES (947, '2025-05-12 03:12:02.857000', '2025-05-08', '心理研究丨心理健康有问题的青少年更爱上网', 'http://psy.china.com.cn/2025-05/08/content_43105017.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/08/content_43105017.htm');
INSERT INTO `news` VALUES (948, '2025-05-12 03:12:02.857000', '2025-05-08', '心理观察｜当代年轻人 “去婚育化” 心理解析', 'http://psy.china.com.cn/2025-05/08/content_43103528.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/08/content_43103528.htm');
INSERT INTO `news` VALUES (949, '2025-05-12 03:12:02.858000', '2025-05-07', '世界说 | 韩国18岁以下心理疾病患者人数四年翻一番引担忧', 'http://psy.china.com.cn/2025-05/07/content_43103254.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/07/content_43103254.htm');
INSERT INTO `news` VALUES (950, '2025-05-12 03:12:02.859000', '2025-05-07', '心理话题|亲密关系要不要一味追求“情绪稳定”？', 'http://psy.china.com.cn/2025-05/07/content_43102964.htm', NULL, NULL, 'http://psy.china.com.cn/2025-05/07/content_43102964.htm');

-- ----------------------------
-- Table structure for scl90_results
-- ----------------------------
DROP TABLE IF EXISTS `scl90_results`;
CREATE TABLE `scl90_results`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `factor_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `positive_average` double NOT NULL,
  `positive_items` int(11) NOT NULL,
  `total_average` double NOT NULL,
  `total_score` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scl90_results
-- ----------------------------
INSERT INTO `scl90_results` VALUES (1, '{\"敌对\":3.5,\"偏执\":2.8333333333333335,\"躯体化\":3.5833333333333335,\"强迫症状\":3.0,\"焦虑\":3.1,\"精神病性\":3.4,\"恐怖\":3.2857142857142856,\"其他\":3.142857142857143,\"抑郁\":2.6923076923076925,\"人际关系敏感\":2.888888888888889}', 3.1333333333333333, 90, 3.1333333333333333, 282, 1);

-- ----------------------------
-- Table structure for scl_factors
-- ----------------------------
DROP TABLE IF EXISTS `scl_factors`;
CREATE TABLE `scl_factors`  (
  `id` int(11) NOT NULL,
  `factor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scl_factors
-- ----------------------------
INSERT INTO `scl_factors` VALUES (1, '躯体化', '反映主观的躯体不适感，包括心血管、胃肠道、呼吸等系统的主述不适，以及头痛、背痛、肌肉酸痛和各种心理不适引发的躯体表现。');
INSERT INTO `scl_factors` VALUES (2, '强迫症状', '与临床强迫症表现的症状、定义基本相同。主要指那种明知没有必要但又无法摆脱的无意义的思想、冲动、行为等表现。');
INSERT INTO `scl_factors` VALUES (3, '人际关系敏感', '主要指某些主观的不自在感和自卑感，尤其是在与他人相比较时更突出。');
INSERT INTO `scl_factors` VALUES (4, '抑郁', '反映的是与临床上抑郁症状群相联系的广泛的概念，包括抑郁苦闷的感情和心境。');
INSERT INTO `scl_factors` VALUES (5, '焦虑', '包括一些通常在临床上明显与焦虑症状相联系的精神症状及体验，如神经过敏、紧张等。');
INSERT INTO `scl_factors` VALUES (6, '敌对', '从思维、情感及行为三方面来反映敌对表现，包括厌烦、争论、摔物等。');
INSERT INTO `scl_factors` VALUES (7, '恐怖', '与传统的恐怖状态或广场恐怖所反映的内容基本一致，包括对出门旅行、空旷场地、人群、公共场合及交通工具等的恐惧。');
INSERT INTO `scl_factors` VALUES (8, '偏执', '偏执是一个复杂的概念，主要指思维方面的异常，如投射性思维、敌对、猜疑、关系妄想等。');
INSERT INTO `scl_factors` VALUES (9, '精神病性', '包括幻听、思维播散、被控制感、思维被插入等反映精神分裂样症状的项目。');
INSERT INTO `scl_factors` VALUES (10, '其他', '主要反映睡眠及饮食情况等其他项目。');

-- ----------------------------
-- Table structure for scl_questions
-- ----------------------------
DROP TABLE IF EXISTS `scl_questions`;
CREATE TABLE `scl_questions`  (
  `id` int(11) NOT NULL,
  `question_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `factor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `factor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scl_questions
-- ----------------------------
INSERT INTO `scl_questions` VALUES (1, '头痛', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (2, '神经过敏，心中不踏实', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (3, '头脑中有不必要的想法或字句盘旋', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (4, '头昏或昏倒', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (5, '对异性的兴趣减退', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (6, '对旁人责备求全', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (7, '感到别人能控制你的思想', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (8, '责怪别人制造麻烦', '偏执', 8);
INSERT INTO `scl_questions` VALUES (9, '忘记性大', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (10, '担心自己的衣饰整齐及仪态的端正', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (11, '容易烦恼和激动', '敌对', 6);
INSERT INTO `scl_questions` VALUES (12, '胸痛', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (13, '害怕空旷的场所或街道', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (14, '感到自己的精力下降，活动减慢', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (15, '想结束自己的生命', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (16, '听到旁人听不到的声音', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (17, '发抖', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (18, '感到大多数人都不可信任', '偏执', 8);
INSERT INTO `scl_questions` VALUES (19, '胃口不好', '其他', 10);
INSERT INTO `scl_questions` VALUES (20, '容易哭泣', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (21, '同异性相处时感到害羞不自在', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (22, '感到受骗，中了圈套或有人想抓您', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (23, '无缘无故地突然感到害怕', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (24, '自己不能控制地大发脾气', '敌对', 6);
INSERT INTO `scl_questions` VALUES (25, '怕单独出门', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (26, '经常责怪自己', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (27, '腰痛', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (28, '感到难以完成任务', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (29, '感到孤独', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (30, '感到苦闷', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (31, '过分担忧', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (32, '对事物不感兴趣', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (33, '感到害怕', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (34, '我的感情容易受到伤害', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (35, '旁人能知道您的私下想法', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (36, '感到别人不理解您不同情您', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (37, '感到人们对你不友好，不喜欢你', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (38, '做事必须做得很慢以保证做得正确', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (39, '心跳得很厉害', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (40, '恶心或胃部不舒服', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (41, '感到比不上他人', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (42, '肌肉酸痛', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (43, '感到有人在监视您谈论您', '偏执', 8);
INSERT INTO `scl_questions` VALUES (44, '难以入睡', '其他', 10);
INSERT INTO `scl_questions` VALUES (45, '做事必须反复检查', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (46, '难以作出决定', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (47, '怕乘电车、公共汽车、地铁或火车', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (48, '呼吸有困难', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (49, '一阵阵发冷或发热', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (50, '因为感到害怕而避开某些东西，场合或活动', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (51, '脑子变空了', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (52, '身体发麻或刺痛', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (53, '喉咙有梗塞感', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (54, '感到对前途没有希望', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (55, '不能集中注意力', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (56, '感到身体的某一部分软弱无力', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (57, '感到紧张或容易紧张', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (58, '感到手或脚发沉', '躯体化', 1);
INSERT INTO `scl_questions` VALUES (59, '想到有关死亡的事', '其他', 10);
INSERT INTO `scl_questions` VALUES (60, '吃得太多', '其他', 10);
INSERT INTO `scl_questions` VALUES (61, '当别人看着您或谈论您时感到不自在', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (62, '有一些不属于您自己的想法', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (63, '有想打人或伤害他人的冲动', '敌对', 6);
INSERT INTO `scl_questions` VALUES (64, '醒得太早', '其他', 10);
INSERT INTO `scl_questions` VALUES (65, '必须反复洗手、点数目或触摸某些东西', '强迫症状', 2);
INSERT INTO `scl_questions` VALUES (66, '睡得不稳不深', '其他', 10);
INSERT INTO `scl_questions` VALUES (67, '有想摔坏或破坏东西的冲动', '敌对', 6);
INSERT INTO `scl_questions` VALUES (68, '有一些别人没有的想法或念头', '偏执', 8);
INSERT INTO `scl_questions` VALUES (69, '感到对别人神经过敏', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (70, '在商店或电影院等人多的地方感到不自在', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (71, '感到任何事情都很难做', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (72, '一阵阵恐惧或惊恐', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (73, '感到在公共场合吃东西很不舒服', '人际关系敏感', 3);
INSERT INTO `scl_questions` VALUES (74, '经常与人争论', '敌对', 6);
INSERT INTO `scl_questions` VALUES (75, '单独一人时神经很紧张', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (76, '别人对您的成绩没有作出恰当的评价', '偏执', 8);
INSERT INTO `scl_questions` VALUES (77, '即使和别人在一起也感到孤单', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (78, '感到坐立不安心神不宁', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (79, '感到自己没有什么价值', '抑郁', 4);
INSERT INTO `scl_questions` VALUES (80, '感到熟悉的东西变成陌生或不象是真的', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (81, '大叫或摔东西', '敌对', 6);
INSERT INTO `scl_questions` VALUES (82, '害怕会在公共场合昏倒', '恐怖', 7);
INSERT INTO `scl_questions` VALUES (83, '感到别人想占您的便宜', '偏执', 8);
INSERT INTO `scl_questions` VALUES (84, '为一些有关\"性\"的想法而很苦恼', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (85, '认为应该因为自己的过错而受到惩罚', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (86, '感到要赶快把事情做完', '焦虑', 5);
INSERT INTO `scl_questions` VALUES (87, '感到自己的身体有严重问题', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (88, '从未感到和其他人很亲近', '精神病性', 9);
INSERT INTO `scl_questions` VALUES (89, '感到自己有罪', '其他', 10);
INSERT INTO `scl_questions` VALUES (90, '感到自己的脑子有毛病', '精神病性', 9);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `mbti_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6`(`username`) USING BTREE,
  UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7`(`email`) USING BTREE,
  UNIQUE INDEX `UK_du5v5sr43g5bfnji4vb8hg5s3`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '2025-02-23 06:17:53.249000', '2640289029@qq.com', '$2a$10$6X4kXGz62MaTX8WoU89lP./rYE4MLFxRLrgpxip1G18Rx3XiBl9re', '13319381001', '2025-04-20 13:24:24.807000', 'admin', 'INFP', 18, '/uploads/avatars/310ee786-4ad5-434f-beaa-5df094d522a8.jpg', 'hello world', '男', '大二');
INSERT INTO `users` VALUES (2, '2025-04-29 01:28:49.084000', 'wangzeyu333411@foxmail.com', '$2a$10$1UzaZLVLBeNRIMPneQVSFuQ0qKgjX2vHrEGOfnciB/erk1F3NI/kC', '13319381002', '2025-04-29 01:28:49.084000', 'wzy', NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
