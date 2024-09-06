package com.sparta.snsproject.dto.relationship;

import com.sparta.snsproject.entity.AskStatus;
import com.sparta.snsproject.entity.Relationship;
import lombok.Getter;

@Getter
public class RelationshipResponseDto {
    private Long send_id;
    private Long receive_id;
    private AskStatus status;

    public RelationshipResponseDto(Relationship saveRelationship) {
        this.send_id = saveRelationship.getSend().getId();
        this.receive_id = saveRelationship.getReceive().getId();
        this.status = saveRelationship.getStatus();
    }
}
