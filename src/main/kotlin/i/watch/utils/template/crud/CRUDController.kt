package i.watch.utils.template.crud

import org.springframework.data.domain.Pageable

/**
 * 通用CRUD Controller
 *
 * @param IN : Any 传入对象
 * @param OUT : Any 传出对象
 * @param ID : Any ID 类型
 * @property service CRUDService<IN, OUT, ID>
 * @constructor
 */
abstract class CRUDController<IN : Any, OUT : CRUDOutputView, ID : Any>(
    protected val service: CRUDService<IN, OUT, ID>
) {
    open fun getAll(pageable: Pageable) = service.select(pageable)

    open fun insert(input: IN) = service.insert(input)

    open fun update(id: ID, input: IN) = service.update(id, input)

    open fun delete(id: ID) = service.delete(id)
}
