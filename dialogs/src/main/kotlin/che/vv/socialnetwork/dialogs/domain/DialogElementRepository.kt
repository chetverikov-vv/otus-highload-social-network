package che.vv.socialnetwork.dialogs.domain

interface DialogElementRepository {
    fun save(element: DialogElement): Result<Unit>
    fun findBy(firstUserId: UserId, secondUserId: UserId): Result<List<DialogElement>>
}