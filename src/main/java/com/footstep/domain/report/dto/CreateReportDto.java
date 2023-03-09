package com.footstep.domain.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReportDto {
    @ApiModelProperty(value = "신고 대상", required = true, example = "0")
    @NotBlank
    private int targetNumber;
    @ApiModelProperty(value = "신고 사유", required = true, example = "0")
    @NotBlank
    private int reasonNumber;
}
