package com.eloli.ocsftp.core;

import com.eloli.sodioncore.dependency.DependencyManager;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;
import com.eloli.sodioncore.orm.AbstractSodionCore;

import java.nio.file.Path;

public interface PlatformAdapter {
    DependencyManager getDependencyManager();
    BaseFileService getBaseFile();
    AbstractSodionCore getSodionCore();
    AbstractLogger getLogger();
    Path getDataPath();
}
