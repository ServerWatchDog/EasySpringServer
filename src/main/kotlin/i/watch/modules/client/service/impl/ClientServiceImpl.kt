package i.watch.modules.client.service.impl

import i.watch.handler.advice.BadRequestException
import i.watch.modules.client.model.db.ClientEntity
import i.watch.modules.client.model.view.client.ClientInsertView
import i.watch.modules.client.model.view.client.ClientResultView
import i.watch.modules.client.repository.ClientGroupRepository
import i.watch.modules.client.repository.ClientRepository
import i.watch.modules.client.service.IClientService
import i.watch.modules.client.service.IClientSessionService
import i.watch.modules.user.repository.UserRepository
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.template.crud.CRUDServiceImpl
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ClientServiceImpl(
    override val repository: ClientRepository,
    private val idGenerator: SnowFlakeUtils,
    private val clientSessionService: IClientSessionService,
    private val userRepository: UserRepository,
    private val groupRepository: ClientGroupRepository
) : IClientService,
    CRUDServiceImpl<ClientInsertView, ClientResultView, Long, ClientEntity>() {
    override fun tableToOutput(table: ClientEntity): ClientResultView {
        val user = table.linkedUser
        return ClientResultView(
            id = table.id.toString(),
            user = ClientResultView.SimpleUserResultView(
                id = user.id.toString(),
                name = user.name,
                email = user.email
            ),
            groups = table.groups
                .map { ClientResultView.SimpleClientGroupResultView(it.id, it.name) },
            token = table.token,
            enabled = table.enable
        )
    }

    override fun inputToTable(table: Optional<ClientEntity>, input: ClientInsertView): ClientEntity {
        val newGroups by lazy {
            groupRepository.findAllByIdIn(input.linkedGroup)
        }
        val newUser by lazy {
            userRepository.findById(input.linkedUser)
                .orElseThrow { BadRequestException("没有此用户！") }
        }
        return table.map {
            if (input.refreshToken) {
                it.token = clientSessionService.newToken()
            }
            it.groups.clear()
            it.groups.addAll(newGroups)
            it.enable = input.enabled
            it.linkedUser = newUser
            it
        }.orElseGet {
            ClientEntity(
                id = idGenerator.nextId(),
                name = input.name,
                token = clientSessionService.newToken(),
                linkedUser = newUser,
                enable = input.enabled
            ).apply {
                groups.addAll(newGroups)
            }
        }
    }
}
