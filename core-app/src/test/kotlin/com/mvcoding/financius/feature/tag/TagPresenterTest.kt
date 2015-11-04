package com.mvcoding.financius.feature.tag

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.subjects.PublishSubject

class TagPresenterTest {
    val titleSubject = PublishSubject.create<String>()
    val colorSubject = PublishSubject.create<Int>()
    val saveSubject = PublishSubject.create<Unit>()
    val tag = aTag()
    val tagsRepository = mock(TagsRepository::class.java)
    val view = mock(TagPresenter.View::class.java)
    val presenter = TagPresenter(tag, tagsRepository)

    @Before
    fun setUp() {
        given(view.onTitleChanged()).willReturn(titleSubject)
        given(view.onColorChanged()).willReturn(colorSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsTitle() {
        presenter.onAttachView(view)

        verify(view).showTitle(tag.title)
    }

    @Test
    fun showsUpdatedTitleWhenTitleIsUpdated() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")

        verify(view).showTitle("updatedTitle")
    }

    @Test
    fun showsUpdatedTitleAfterReattach() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")
        presenter.onDetachView(view)
        reset(view)
        presenter.onAttachView(view)

        verify(view).showTitle("updatedTitle")
    }

    @Test
    fun showsColor() {
        presenter.onAttachView(view)

        verify(view).showColor(tag.color)
    }

    @Test
    fun showsUpdatedColorWhenColorIsUpdated() {
        presenter.onAttachView(view)

        updateColor(10)

        verify(view).showColor(10)
    }

    @Test
    fun showsUpdatedColorAfterReattach() {
        presenter.onAttachView(view)

        updateColor(10)
        presenter.onDetachView(view)
        reset(view)
        presenter.onAttachView(view)

        verify(view).showColor(10)
    }

    @Test
    fun savesTagWhenValidationSucceeds() {
        presenter.onAttachView(view)

        save()

        verify(tagsRepository).save(tag)
    }

    @Test
    fun startsResultWhenTagIsSavedSuccessfully() {
        presenter.onAttachView(view)

        save()

        verify(view).startResult(tag)
    }

    @Test
    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
        presenter.onAttachView(view)
        updateTitle("")

        save()

        verify(view, never()).startResult(any())
        verify(view).showTitleCannotBeEmptyError()
    }

    private fun updateTitle(title: String) {
        titleSubject.onNext(title)
    }

    private fun updateColor(color: Int) {
        colorSubject.onNext(color)
    }

    private fun save() {
        saveSubject.onNext(Unit)
    }
}
