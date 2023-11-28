package com.pblgllgs.message;
/*
 *
 * @author pblgl
 * Created on 28-11-2023
 *
 */

import com.pblgllgs.service.TrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingStatusUpdated {
    UUID orderId;

    TrackingStatus status;

}
