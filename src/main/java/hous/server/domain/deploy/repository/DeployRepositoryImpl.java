package hous.server.domain.deploy.repository;

import static hous.server.domain.deploy.QDeploy.deploy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.deploy.Deploy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeployRepositoryImpl implements DeployRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Deploy findDeployByOs(String os) {
        return queryFactory
                .selectFrom(deploy)
                .where(
                        deploy.os.eq(os)
                )
                .fetchOne();
    }
}
