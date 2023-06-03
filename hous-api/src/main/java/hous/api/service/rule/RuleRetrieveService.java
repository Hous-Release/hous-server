package hous.api.service.rule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomServiceUtils;
import hous.api.service.rule.dto.response.RuleAddableResponse;
import hous.api.service.rule.dto.response.RuleInfoResponse;
import hous.api.service.rule.dto.response.RulesResponse;
import hous.api.service.user.UserServiceUtils;
import hous.common.constant.Constraint;
import hous.core.domain.room.Room;
import hous.core.domain.rule.Rule;
import hous.core.domain.rule.mysql.RuleRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RuleRetrieveService {

	private final UserRepository userRepository;
	private final RuleRepository ruleRepository;

	public RulesResponse getRulesInfo(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		return RulesResponse.of(room.getRules());
	}

	public RuleAddableResponse getRuleAddable(Long userId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		return RuleAddableResponse.of(room.getRules().size() < Constraint.RULE_COUNT_MAX);
	}

	public RuleInfoResponse getRuleInfo(Long userId, Long ruleId) {
		User user = UserServiceUtils.findUserById(userRepository, userId);
		Room room = RoomServiceUtils.findParticipatingRoom(user);
		Rule rule = RuleServiceUtils.findRuleByIdAndRoom(ruleRepository, ruleId, room);
		return RuleInfoResponse.of(rule);
	}
}
