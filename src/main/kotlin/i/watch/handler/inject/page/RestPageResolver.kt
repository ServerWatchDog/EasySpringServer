package i.watch.handler.inject.page

import org.springframework.core.MethodParameter
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * 分页信息查询
 */
@Component
class RestPageResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(RestPage::class.java) &&
            parameter.parameter.type == Pageable::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val restPage = parameter.getParameterAnnotation(RestPage::class.java)!!
        val index = webRequest.getParameter("index")?.toInt() ?: 0
        val size = webRequest.getParameter("size")?.toInt() ?: restPage.size
        return PageRequest.of(index, size)
    }
}
