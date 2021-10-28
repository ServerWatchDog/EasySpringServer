package i.watch.utils.template.crud

import i.watch.handler.inject.encrypt.CryptRequestBody
import i.watch.handler.inject.page.RestPage
import i.watch.utils.template.PageView
import i.watch.utils.template.SimpleView
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

/**
 * 带动态加、解密的 CRUD Controller
 */
interface CryptCRUDApi<IN : Any, OUT : CRUDOutputView, ID : Any> {
    @GetMapping("")
    fun getAll(@RestPage pageable: Pageable): PageView<OUT>

    @PostMapping("")
    fun insert(@CryptRequestBody input: IN): OUT

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: ID, @CryptRequestBody input: IN): OUT

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: ID): SimpleView<Boolean>
}
