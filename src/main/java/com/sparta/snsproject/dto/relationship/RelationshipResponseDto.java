package com.sparta.snsproject.dto.relationship;

import com.sparta.snsproject.entity.AskStatus;
import com.sparta.snsproject.entity.Relationship;
import lombok.Getter;

@Getter
public class RelationshipResponseDto {
    private Long asking_id;
    private Long asked_id;
    private AskStatus status;

    public RelationshipResponseDto(Relationship saveRelationship) {
        this.asking_id = saveRelationship.getAsking().getId();
        this.asked_id = saveRelationship.getAsked().getId();
        this.status = saveRelationship.getStatus();
    }
}
