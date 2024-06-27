[
<#list notes as note>
    {
    "id": ${note.id},
    "title": "${note.title}",
    "sortOrder": ${note.sortOrder},
    "isPinned": ${note.isPinned?string('true', 'false')},
    "isArchived": ${note.isArchived?string('true', 'false')}
    }
    <#if note_has_next>,</#if>
</#list>
]
