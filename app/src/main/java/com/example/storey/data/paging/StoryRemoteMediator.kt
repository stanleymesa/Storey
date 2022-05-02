package com.example.storey.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.database.RemoteKeys
import com.example.storey.data.remote.api.ApiServices
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.helper.wrapEspressoIdlingResource

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val database: MainDatabase,
    private val apiServices: ApiServices,
) : RemoteMediator<Int, ListStoryItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>,
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {

            wrapEspressoIdlingResource {

                val responseData = apiServices.getAllStories(
                    token = token,
                    page = page,
                    size = state.config.pageSize
                )
                val responseBody = responseData.body()!!

                val endOfPaginationReached = responseBody.listStory.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeysDao().deleteRemoteKeys()
                        database.listStoryitemDao().deleteAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = responseBody.listStory.map {
                        RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    database.remoteKeysDao().insertAll(keys)
                    database.listStoryitemDao().insertListStory(responseBody.listStory)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

            }


        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}