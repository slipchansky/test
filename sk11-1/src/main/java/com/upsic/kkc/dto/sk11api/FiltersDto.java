package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class FiltersDto {

    private Set<String> typeFilter;

    private Set<UUID> categoryUidFilter;

}
