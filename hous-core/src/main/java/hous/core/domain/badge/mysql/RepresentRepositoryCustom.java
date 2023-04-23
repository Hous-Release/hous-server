package hous.core.domain.badge.mysql;

import hous.core.domain.badge.Represent;
import hous.core.domain.user.Onboarding;

public interface RepresentRepositoryCustom {
    Represent findRepresentByOnboarding(Onboarding onboarding);

}
