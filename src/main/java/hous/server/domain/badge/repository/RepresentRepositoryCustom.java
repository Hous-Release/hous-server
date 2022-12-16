package hous.server.domain.badge.repository;

import hous.server.domain.badge.Represent;
import hous.server.domain.user.Onboarding;

public interface RepresentRepositoryCustom {
    Represent findRepresentByOnboarding(Onboarding onboarding);

}
