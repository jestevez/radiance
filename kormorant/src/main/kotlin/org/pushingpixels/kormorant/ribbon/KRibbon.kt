/*
 * Copyright (c) 2018 Radiance Kormorant Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Radiance Kormorant Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.kormorant.ribbon

import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame
import org.pushingpixels.flamingo.api.ribbon.RibbonTask
import org.pushingpixels.kormorant.FlamingoElementMarker
import org.pushingpixels.kormorant.NullableDelegate
import org.pushingpixels.neon.icon.ResizableIcon

@FlamingoElementMarker
class KRibbonTaskContainer {
    val tasks = arrayListOf<KRibbonTask>()

    operator fun KRibbonTask.unaryPlus() {
        this@KRibbonTaskContainer.tasks.add(this)
    }
}

@FlamingoElementMarker
class KRibbonFrame {
    private var ribbonFrame: JRibbonFrame? = null

    var title: String? by NullableDelegate(ribbonFrame)
    var applicationIcon: ResizableIcon? by NullableDelegate(ribbonFrame)
    private val tasks = KRibbonTaskContainer()

    fun tasks(init: KRibbonTaskContainer.() -> Unit) {
        tasks.init()
    }

    fun asRibbonFrame(): JRibbonFrame {
        if (ribbonFrame != null) {
            throw IllegalStateException("This method can only be called once")
        }

        ribbonFrame = JRibbonFrame(title)
        ribbonFrame!!.applicationIcon = applicationIcon
        for (task in tasks.tasks) {
            ribbonFrame!!.ribbon.addTask(task.asRibbonTask())
        }

        return ribbonFrame!!
    }
}

fun ribbonFrame(init: KRibbonFrame.() -> Unit): KRibbonFrame {
    val ribbonFrame = KRibbonFrame()
    ribbonFrame.init()
    return ribbonFrame
}