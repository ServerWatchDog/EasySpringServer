package i.watch.utils.template.crud

import i.watch.handler.advice.BadRequestException
import i.watch.handler.advice.NotFoundException
import i.watch.utils.template.PageView
import i.watch.utils.template.SimpleView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import javax.transaction.Transactional

abstract class CRUDServiceImpl<IN : Any, OUT : CRUDOutputView, ID : Any, TABLE : Any> :
    CRUDService<IN, OUT, ID> {

    abstract val repository: JpaRepository<TABLE, ID>

    abstract fun tableToOutput(table: TABLE): OUT
    abstract fun inputToTable(table: Optional<TABLE>, input: IN): TABLE

    open fun afterWriteHook(table: TABLE) {}

    @Transactional
    override fun select(pageable: Pageable): PageView<OUT> {
        return repository.findAll(pageable)
            .map { tableToOutput(it) }
            .let {
                PageView(
                    it.toList(),
                    pageable.pageNumber,
                    repository.count() / pageable.pageSize
                )
            }
    }

    @Transactional
    override fun insert(input: IN): OUT {
        val insertTable = try {
            inputToTable(Optional.empty(), input)
        } catch (e: Exception) {
            throw BadRequestException("添加错误！${e.message ?: ""}")
        }
        val saved = repository.save(insertTable)
        afterWriteHook(saved)
        return tableToOutput(saved)
    }

    @Transactional
    override fun update(id: ID, input: IN): OUT {
        val table = repository.findById(id)
            .orElseThrow { throw BadRequestException("更新错误！未找到 id 为 $id 的数据.") }

        val updateTable = try {
            inputToTable(Optional.of(table), input)
        } catch (e: Exception) {
            throw BadRequestException("更新错误！${e.message ?: ""}")
        }
        val saved = repository.save(updateTable)
        afterWriteHook(saved)
        return tableToOutput(saved)
    }

    @Transactional
    override fun delete(id: ID): SimpleView<Boolean> {
        val table = repository.findById(id)
            .orElseThrow { throw NotFoundException("删除错误！未找到 id 为 $id 的数据.") }

        repository.delete(table)
        afterWriteHook(table)
        return SimpleView(true)
    }
}
