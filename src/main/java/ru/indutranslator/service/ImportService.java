package ru.indutranslator.service;

import ru.indutranslator.domain.dto.ImportResultDto;

import java.util.List;

public interface ImportService {

    ImportResultDto importTask(String sourceSystem, String externalId, Long userId);

    ImportResultDto importDocument(String sourceSystem, String externalId, Long userId);

    ImportResultDto importBatch(String sourceSystem, String filePath, Long userId);

    List<ImportResultDto> getImportHistory(int page, int size);

    List<ImportResultDto> getFailedImports(int page, int size);

    void retryImport(Long id, Long userId);

    void deleteImport(Long id, Long userId);

    ImportResultDto getImportStatus(Long id);
}
