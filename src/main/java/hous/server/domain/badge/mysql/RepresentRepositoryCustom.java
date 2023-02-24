package hous.server.domain.badge.mysql;

import hous.server.domain.badge.Represent;
import hous.server.domain.user.Onboarding;

public interface RepresentRepositoryCustom {
    Represent findRepresentByOnboarding(Onboarding onboarding);

}
