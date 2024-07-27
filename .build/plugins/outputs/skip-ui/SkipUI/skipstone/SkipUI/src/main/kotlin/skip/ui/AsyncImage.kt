// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import skip.foundation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import android.webkit.MimeTypeMap
import coil.fetch.Fetcher
import coil.fetch.FetchResult
import coil.fetch.SourceResult
import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.request.Options
import okio.buffer
import okio.source

class AsyncImage: View {
    internal val url: URL?
    internal val scale: Double
    internal val content: (AsyncImagePhase) -> View

    constructor(url: URL?, scale: Double = 1.0) {
        this.url = url.sref()
        this.scale = scale
        this.content = l@{ phase ->
            when (phase) {
                is AsyncImagePhase.EmptyCase -> return@l Companion.defaultPlaceholder()
                is AsyncImagePhase.FailureCase -> return@l Companion.defaultPlaceholder()
                is AsyncImagePhase.SuccessCase -> {
                    val image = phase.associated0
                    return@l image
                }
            }
        }
    }

    constructor(url: URL?, scale: Double = 1.0, content: (Image) -> View, placeholder: () -> View) {
        this.url = url.sref()
        this.scale = scale
        this.content = l@{ phase ->
            when (phase) {
                is AsyncImagePhase.EmptyCase -> return@l placeholder()
                is AsyncImagePhase.FailureCase -> return@l placeholder()
                is AsyncImagePhase.SuccessCase -> {
                    val image = phase.associated0
                    return@l content(image)
                }
            }
        }
    }

    constructor(url: URL?, scale: Double = 1.0, transaction: Any? = null, content: (AsyncImagePhase) -> View) {
        this.url = url.sref()
        this.scale = scale
        this.content = content
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        if (url == null) {
            this.content(AsyncImagePhase.empty).Compose(context)
            return
        }

        val urlString = url.absoluteString
        // Coil does not automatically handle embedded jar URLs like jar:file:/data/app/…/base.apk!/showcase/module/Resources/swift-logo.png, so
        // we add a custom `JarURLFetcher` fetcher that will handle loading the URL. Otherwise use Coil's default URL string handling
        val requestSource: Any = (if (JarURLFetcher.isJarURL(url)) url else urlString).sref()
        val model = ImageRequest.Builder(LocalContext.current)
            .fetcherFactory(JarURLFetcher.Factory())
            .decoderFactory(PdfDecoder.Factory())
            .data(requestSource)
            .size(Size.ORIGINAL)
            .memoryCacheKey(urlString)
            .diskCacheKey(urlString)
            .build()
        SubcomposeAsyncImage(model = model, contentDescription = null, loading = { _ -> content(AsyncImagePhase.empty).Compose(context = context) }, success = { state ->
            val image = Image(painter = this.painter, scale = scale)
            content(AsyncImagePhase.success(image)).Compose(context = context)
        }, error = { state -> content(AsyncImagePhase.failure(ErrorException(cause = state.result.throwable))).Compose(context = context) })
    }

    companion object {

        internal fun defaultPlaceholder(): View {
            return ComposeBuilder { composectx: ComposeContext -> Color.placeholder.Compose(composectx) }
        }
    }
}

sealed class AsyncImagePhase {
    class EmptyCase: AsyncImagePhase() {
    }
    class SuccessCase(val associated0: Image): AsyncImagePhase() {
    }
    class FailureCase(val associated0: Error): AsyncImagePhase() {
    }

    val image: Image?
        get() {
            when (this) {
                is AsyncImagePhase.SuccessCase -> {
                    val image = this.associated0
                    return image
                }
                else -> return null
            }
        }

    val error: Error?
        get() {
            when (this) {
                is AsyncImagePhase.FailureCase -> {
                    val error = this.associated0
                    return error
                }
                else -> return null
            }
        }

    companion object {
        val empty: AsyncImagePhase = EmptyCase()
        fun success(associated0: Image): AsyncImagePhase = SuccessCase(associated0)
        fun failure(associated0: Error): AsyncImagePhase = FailureCase(associated0)
    }
}

/// A Coil fetcher that handles `skip.foundation.URL` instances for the `jar:` scheme.
internal class JarURLFetcher: Fetcher {
    private val data: URL
    private val options: Options

    internal constructor(data: URL, options: Options) {
        this.data = data.sref()
        this.options = options.sref()
    }

    override suspend fun fetch(): FetchResult = Async.run l@{
        return@l SourceResult(source = ImageSource(source = data.kotlin().toURL().openConnection().getInputStream().source().buffer(), context = options.context), mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(data.absoluteString)), dataSource = DataSource.DISK)
    }

    internal class Factory: Fetcher.Factory<URL> {
        override fun create(data: URL, options: Options, imageLoader: ImageLoader): Fetcher? {
            if ((!JarURLFetcher.isJarURL(data))) {
                return null
            }
            return JarURLFetcher(data = data, options = options)
        }
    }

    companion object {

        internal fun isJarURL(url: URL): Boolean = url.absoluteString.hasPrefix("jar")
    }
}


internal open class PdfDecoder: coil.decode.Decoder {
    internal val sourceResult: coil.fetch.SourceResult
    internal val options: coil.request.Options

    internal class Factory: coil.decode.Decoder.Factory {
        override fun create(result: coil.fetch.SourceResult, options: coil.request.Options, imageLoader: coil.ImageLoader): coil.decode.Decoder? {
            logger.log("PdfDecoder.Factory.create result=${result} options=${options} imageLoader=${imageLoader}")
            return PdfDecoder(sourceResult = result, options = options)
        }
    }

    internal constructor(sourceResult: coil.fetch.SourceResult, options: coil.request.Options) {
        this.sourceResult = sourceResult.sref()
        this.options = options.sref()
    }

    override suspend fun decode(): coil.decode.DecodeResult? = Async.run l@{
        val deferactions_0: MutableList<() -> Unit> = mutableListOf()
        try {
            val src: coil.decode.ImageSource = sourceResult.source.sref()
            val source: okio.BufferedSource = src.source()

            // make sure it is a PDF image by scanning for "%PDF-" (25 50 44 46 2D)
            val peek = source.peek()
            // logger.log("PdfDecoder.decode peek \(peek.readByte()) \(peek.readByte()) \(peek.readByte()) \(peek.readByte()) \(peek.readByte())")

            if (peek.readByte() != Byte(0x25)) {
                return@l null // %
            } // %
            if (peek.readByte() != Byte(0x50)) {
                return@l null // P
            } // P
            if (peek.readByte() != Byte(0x44)) {
                return@l null // D
            } // D
            if (peek.readByte() != Byte(0x46)) {
                return@l null // F
            } // F
            if (peek.readByte() != Byte(0x2D)) {
                return@l null // -
            } // -

            // Unfortunately, PdfRenderer requires a ParcelFileDescriptor, which can only be created from an actual file, and not the JarInputStream from which we load assets from the .apk; so we need to write the PDF out to a temporary file in order to be able to render the PDF to a Bitmap that Coil can use
            // Fortunately, even through we are loading from a buffer, Coil's ImageSource.file() function will: “Return a Path that resolves to a file containing this ImageSource's data. If this image source is backed by a BufferedSource, a temporary file containing this ImageSource's data will be created.”
            val imageFile = src.file().toFile()
            logger.log("PdfDecoder.decode result=${sourceResult} options=${options} imageFile=${imageFile}")

            val parcelFileDescriptor = android.os.ParcelFileDescriptor.open(imageFile, android.os.ParcelFileDescriptor.MODE_READ_ONLY)
            deferactions_0.add {
                parcelFileDescriptor.close()
            }

            val pdfRenderer = android.graphics.pdf.PdfRenderer(parcelFileDescriptor)
            deferactions_0.add {
                pdfRenderer.close()
            }

            val page = pdfRenderer.openPage(0)
            deferactions_0.add {
                page.close()
            }

            val width = page.width.sref()
            val height = page.height.sref()
            val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            val drawable = android.graphics.drawable.BitmapDrawable(options.context.resources, bitmap)
            return@l coil.decode.DecodeResult(drawable = drawable, isSampled = false)
        } finally {
            deferactions_0.asReversed().forEach { it.invoke() }
        }
    }
}
