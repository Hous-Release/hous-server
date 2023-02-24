package hous.server.domain.deploy.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.deploy.Deploy;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.deploy.QDeploy.deploy;

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
