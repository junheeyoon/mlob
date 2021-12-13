package com.asianaidt.ict.analyca.system.dockercore.dto.registry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class Catalog {
    private List<String> repositories;
}
