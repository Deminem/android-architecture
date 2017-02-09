/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskViewModel;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository;
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource;
import com.example.android.architecture.blueprints.todoapp.statistics.StatisticsViewModel;
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailViewModel;
import com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel;
import com.example.android.architecture.blueprints.todoapp.util.providers.BaseNavigationProvider;
import com.example.android.architecture.blueprints.todoapp.util.providers.BaseResourceProvider;
import com.example.android.architecture.blueprints.todoapp.util.providers.NavigationProvider;
import com.example.android.architecture.blueprints.todoapp.util.providers.ResourceProvider;
import com.example.android.architecture.blueprints.todoapp.util.schedulers.BaseSchedulerProvider;
import com.example.android.architecture.blueprints.todoapp.util.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link TasksDataSource} at compile time.
 */
public class Injection {

    @NonNull
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context, provideSchedulerProvider()));
    }

    @NonNull
    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    @NonNull
    public static BaseResourceProvider provideResourceProvider(@NonNull Context context) {
        return new ResourceProvider(context);
    }

    @NonNull
    public static StatisticsViewModel provideStatisticsViewModel(@NonNull Context context) {
        return new StatisticsViewModel(provideTasksRepository(context),
                provideResourceProvider(context));
    }

    @NonNull
    public static TaskDetailViewModel provideTaskDetailsViewModel(
            @Nullable String taskId,
            @NonNull Context context) {
        return new TaskDetailViewModel(taskId, provideTasksRepository(context),
                provideResourceProvider(context));
    }

    @NonNull
    public static BaseNavigationProvider provideNavigationProvider(@NonNull Activity activity) {
        return new NavigationProvider(activity);
    }

    @NonNull
    public static AddEditTaskViewModel provideAddEditTaskViewModel(@Nullable String taskId,
                                                                   @NonNull Activity activity) {
        Context appContext = activity.getApplicationContext();
        return new AddEditTaskViewModel(taskId, provideTasksRepository(appContext),
                provideNavigationProvider(activity));
    }

    @NonNull
    public static TasksViewModel provideTasksViewModel(@NonNull Activity activity) {
        Context appContext = activity.getApplicationContext();
        return new TasksViewModel(provideTasksRepository(appContext),
                provideNavigationProvider(activity), provideSchedulerProvider());
    }
}
