package com.sample.simpsonsviewer.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RequestPermissionsCompleted

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PermissionExplanationRequired
