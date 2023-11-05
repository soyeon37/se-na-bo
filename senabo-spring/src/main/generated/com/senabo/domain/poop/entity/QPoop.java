package com.senabo.domain.poop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPoop is a Querydsl query type for Poop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoop extends EntityPathBase<Poop> {

    private static final long serialVersionUID = 1730237752L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPoop poop = new QPoop("poop");

    public final com.senabo.common.audit.QBaseEntity _super = new com.senabo.common.audit.QBaseEntity(this);

    public final BooleanPath cleanYn = createBoolean("cleanYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.senabo.domain.member.entity.QMember memberId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QPoop(String variable) {
        this(Poop.class, forVariable(variable), INITS);
    }

    public QPoop(Path<? extends Poop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPoop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPoop(PathMetadata metadata, PathInits inits) {
        this(Poop.class, metadata, inits);
    }

    public QPoop(Class<? extends Poop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberId = inits.isInitialized("memberId") ? new com.senabo.domain.member.entity.QMember(forProperty("memberId")) : null;
    }

}

